package com.github.damianszwed.fishky.flashcard.service.configuration;

import com.github.damianszwed.fishky.flashcard.service.adapter.search.elastic.ElasticSearchFlashcardSearchService;
import com.github.damianszwed.fishky.flashcard.service.adapter.search.elastic.ElasticSearchRename;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolderService;
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
  ElasticSearchRename elasticSearchRename(
      @Value("${fishky.elasticsearch.uri}") String elasticSearchUri,
      @Value("${fishky.elasticsearch.index:flashcards-000001") String index) {
    final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
    final URI connUri = URI.create(elasticSearchUri);
    final String[] auth = connUri.getUserInfo().split(":");
    credentialsProvider.setCredentials(AuthScope.ANY,
        new UsernamePasswordCredentials(auth[0], auth[1]));

    return new ElasticSearchRename(index, credentialsProvider, connUri);
  }

  @Bean
  FlashcardSearchService flashcardSearchService(
      FlashcardFolderService flashcardFolderStorage,
      ElasticSearchRename elasticSearchRename) {
    return new ElasticSearchFlashcardSearchService(flashcardFolderStorage, elasticSearchRename);
  }
}
