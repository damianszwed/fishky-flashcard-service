package com.github.damianszwed.fishky.flashcard.service.configuration;

import com.github.damianszwed.fishky.flashcard.service.adapter.search.ElasticSearchFlashcardSearchService;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardSearchService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("elasticsearch")
public class ElasticSearchConfiguration {

  @Bean
  FlashcardSearchService flashcardSearchService() {
    return new ElasticSearchFlashcardSearchService();
  }
}
