package com.github.damianszwed.fishky.flashcard.service.configuration;

import com.github.damianszwed.fishky.flashcard.service.adapter.storage.elastic.ElasticSearchFlashcardRestHighLevelClient;
import com.github.damianszwed.fishky.flashcard.service.adapter.storage.elastic.ElasticSearchFlashcardSearchService;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolderStorage;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardSearchService;
import java.net.URI;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("elasticsearch")
public class ElasticSearchConfiguration {

  @Bean
  ElasticSearchFlashcardRestHighLevelClient elasticSearchFlashcardRestHighLevelClient(
      @Value("${fishky.elasticsearch.uri}") String elasticSearchUri,
      @Value("${fishky.elasticsearch.index:flashcards-000001}") String index) {
    final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
    final URI connUri = URI.create(elasticSearchUri);
    final String[] auth = connUri.getUserInfo().split(":");
    credentialsProvider.setCredentials(AuthScope.ANY,
        new UsernamePasswordCredentials(auth[0], auth[1]));

    return new ElasticSearchFlashcardRestHighLevelClient(index, credentialsProvider, connUri);
  }

  @Bean
  FlashcardSearchService flashcardSearchService(
      FlashcardFolderStorage flashcardFolderStorage,
      ElasticSearchFlashcardRestHighLevelClient elasticSearchFlashcardRestHighLevelClient) {
    return new ElasticSearchFlashcardSearchService(flashcardFolderStorage,
        elasticSearchFlashcardRestHighLevelClient);
  }
}
