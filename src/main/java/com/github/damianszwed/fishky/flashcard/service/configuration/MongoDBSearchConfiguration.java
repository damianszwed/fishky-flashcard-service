package com.github.damianszwed.fishky.flashcard.service.configuration;

import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardSearchService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import reactor.core.publisher.Flux;

@Configuration
@Profile("!elasticsearch")
public class MongoDBSearchConfiguration {

  @Bean
  FlashcardSearchService flashcardSearchService() {
    return (owner, text) -> Flux.empty();
  }
}
