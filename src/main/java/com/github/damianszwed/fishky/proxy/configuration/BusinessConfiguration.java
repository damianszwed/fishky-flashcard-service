package com.github.damianszwed.fishky.proxy.configuration;

import com.github.damianszwed.fishky.proxy.business.FlashcardProviderFlow;
import com.github.damianszwed.fishky.proxy.port.flashcard.EventSource;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BusinessConfiguration {

  @Bean
  FlashcardProviderFlow flashcardProviderFlow(
      FlashcardProvider flashcardProvider) {
    return new FlashcardProviderFlow(flashcardProvider);
  }

  @Bean
  EventSource eventSource(FlashcardProviderFlow flashcardProviderFlow) {
    return flashcardProviderFlow;
  }

}
