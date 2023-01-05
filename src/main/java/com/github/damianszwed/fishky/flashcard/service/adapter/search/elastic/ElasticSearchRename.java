package com.github.damianszwed.fishky.flashcard.service.adapter.search.elastic;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Slf4j
public class ElasticSearchRename {

  private final CredentialsProvider credentialsProvider;
  private final String index;
  private final URI connUri;

  public ElasticSearchRename(String index, CredentialsProvider credentialsProvider, URI connUri) {
    this.credentialsProvider = credentialsProvider;
    this.index = index;
    this.connUri = connUri;
  }

  public Flux<Tuple2<String, String>> search(String owner, String text) {
    SearchRequest searchRequest = new SearchRequest(index);
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    searchSourceBuilder.query(QueryBuilders.boolQuery()
        .must(
            QueryBuilders.multiMatchQuery(text, "folderName", "question", "answers").fuzziness(
                Fuzziness.AUTO))
        .filter(QueryBuilders.termQuery("owner", owner)));
    searchRequest.source(searchSourceBuilder);
    //TODO(Damian.Szwed) refactor this ugly code
    RestHighLevelClient restHighLevelClient = new RestHighLevelClient(
        RestClient.builder(new HttpHost(connUri.getHost(), connUri.getPort(), connUri.getScheme()))
            .setHttpClientConfigCallback(
                httpAsyncClientBuilder -> httpAsyncClientBuilder.setDefaultCredentialsProvider(
                        credentialsProvider)
                    .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())));
    //TODO(Damian.Szwed) refactor this ugly code
    return Flux.create(sink -> {
      restHighLevelClient.searchAsync(searchRequest, RequestOptions.DEFAULT,
          new ActionListener<>() {
            @Override
            public void onResponse(SearchResponse searchResponse) {
              if (RestStatus.OK.equals(searchResponse.status())) {
                final SearchHits hits = searchResponse.getHits();
                log.info("User {} looked for {} and found {} hits.", owner, text,
                    hits.getHits().length);
                Arrays.stream(hits.getHits()).forEach(
                    documentFields -> sink.next(Tuples.of(documentFields.getId(),
                        String.valueOf(documentFields.getSourceAsMap().get("folderId"))))
                );
                sink.complete();
              } else {
                sink.error(new RuntimeException("Wrong search status: " + searchResponse.status()));
              }
              try {
                restHighLevelClient.close();
              } catch (IOException e) {
                log.error("On close", e);
                sink.error(e);
              }
            }

            @Override
            public void onFailure(Exception e) {
              log.error("On failure", e);
              sink.error(e);
            }
          });
    });
  }
}
