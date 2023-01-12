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
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Slf4j
public class ElasticSearchFlashcardRestHighLevelClient {

  private final CredentialsProvider credentialsProvider;
  private final String index;
  private final URI connUri;

  public ElasticSearchFlashcardRestHighLevelClient(
      String index, CredentialsProvider credentialsProvider, URI connUri) {
    this.credentialsProvider = credentialsProvider;
    this.index = index;
    this.connUri = connUri;
  }

  public Flux<Tuple2<String, String>> search(String owner, String text) {
    final SearchRequest searchRequest = getSearchRequest(owner, text);

    return Flux.create(sink -> {
      final RestHighLevelClient restHighLevelClient = getRestHighLevelClient();
      restHighLevelClient.searchAsync(searchRequest, RequestOptions.DEFAULT,
          getSearchListener(owner, text, sink, restHighLevelClient));
    });
  }

  public Mono<Boolean> reindex() {
    return Mono.just(true);
  }

  private RestHighLevelClient getRestHighLevelClient() {
    return new RestHighLevelClient(
        RestClient.builder(new HttpHost(connUri.getHost(), connUri.getPort(), connUri.getScheme()))
            .setHttpClientConfigCallback(
                httpAsyncClientBuilder -> httpAsyncClientBuilder.setDefaultCredentialsProvider(
                        credentialsProvider)
                    .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())));
  }

  private SearchRequest getSearchRequest(String owner, String text) {
    final SearchRequest searchRequest = new SearchRequest(index);
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    searchSourceBuilder.query(QueryBuilders.boolQuery()
        .must(
            QueryBuilders.multiMatchQuery(text, "folderName", "question", "answers").fuzziness(
                Fuzziness.AUTO))
        .filter(QueryBuilders.termQuery("owner", owner)));
    searchRequest.source(searchSourceBuilder);
    return searchRequest;
  }

  private static ActionListener<SearchResponse> getSearchListener(
      String owner, String text, FluxSink<Tuple2<String, String>> sink,
      RestHighLevelClient restHighLevelClient) {
    return new ActionListener<>() {
      @Override
      public void onResponse(SearchResponse searchResponse) {
        if (RestStatus.OK.equals(searchResponse.status())) {
          final SearchHits hits = searchResponse.getHits();
          log.info("User {} looked for {} and found {} hits.", owner, text,
              hits.getHits().length);
          Arrays.stream(hits.getHits()).forEach(documentFields ->
              sink.next(getFlashcardIdAndFolderId(documentFields))
          );
          sink.complete();
        } else {
          sink.error(
              new RuntimeException("Wrong search status: " + searchResponse.status()));
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
    };
  }

  private static Tuple2<String, String> getFlashcardIdAndFolderId(SearchHit searchHit) {
    return Tuples.of(searchHit.getId(),
        String.valueOf(searchHit.getSourceAsMap().get("folderId")));
  }
}
