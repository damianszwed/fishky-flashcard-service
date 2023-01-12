package com.github.damianszwed.fishky.flashcard.service.configuration;

import com.github.damianszwed.fishky.flashcard.service.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Configuration
@Profile("!elasticsearch")
@Slf4j
public class MongoSearchConfiguration {

  @Bean
  FlashcardSearchService flashcardSearchService() {
    return new FlashcardSearchService() {
      @Override
      public Flux<Flashcard> search(String owner, String text) {
        //TODO(Damian.Szwed) MongoDB search
        return Flux.empty();
      }

      @Override
      public Mono<Boolean> reindex() {
        log.warn("Reindex for MongoDB is not needed.");
        return Mono.empty();
      }
    };
  }
}
