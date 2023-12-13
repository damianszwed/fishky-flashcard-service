package com.github.damianszwed.fishky.flashcard.service.component.driver;

import com.github.damianszwed.fishky.flashcard.service.adapter.storage.entity.FlashcardFolder;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.EventSource;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardSearchService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Configuration
public class SpringTestConfiguration {

  @Bean
  FishkyFlashcardServiceDriver fishkyFlashcardServiceDriver(
      WebTestClient webTestClient,
      EventSource<FlashcardFolder> flashcardFoldersEventSource) {
    return new FishkyFlashcardServiceDriver(webTestClient, flashcardFoldersEventSource);
  }

  @Bean
  FlashcardSearchService flashcardSearchService() {
    return new FlashcardSearchService() {
      @Override
      public Flux<FlashcardFolder> search(String owner, String text) {
        throw new UnsupportedOperationException();
      }

      @Override
      public Mono<Void> reindex() {
        throw new UnsupportedOperationException();
      }
    };
  }

}
