package com.github.damianszwed.fishky.proxy.component.driver;

import com.github.damianszwed.fishky.proxy.port.flashcard.EventSource;
import com.github.damianszwed.fishky.proxy.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardFolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.web.reactive.server.WebTestClient;

@Configuration
public class SpringTestConfiguration {
  @Bean
  FishkyProxyDriver fishkyProxyDriver(
      WebTestClient webTestClient,
      EventSource<Flashcard> eventSource,
      EventSource<FlashcardFolder> flashcardFoldersEventSource) {
    return new FishkyProxyDriver(webTestClient, eventSource, flashcardFoldersEventSource);
  }

}
