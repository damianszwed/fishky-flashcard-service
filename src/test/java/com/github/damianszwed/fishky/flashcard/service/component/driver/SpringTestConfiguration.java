package com.github.damianszwed.fishky.flashcard.service.component.driver;

import com.github.damianszwed.fishky.flashcard.service.port.flashcard.EventSource;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.web.reactive.server.WebTestClient;

@Configuration
public class SpringTestConfiguration {

  @Bean
  FishkyFlashcardServiceDriver fishkyFlashcardServiceDriver(
      WebTestClient webTestClient,
      EventSource<FlashcardFolder> flashcardFoldersEventSource) {
    return new FishkyFlashcardServiceDriver(webTestClient, flashcardFoldersEventSource);
  }

}
