package com.github.damianszwed.fishky.proxy.configuration;

import com.github.damianszwed.fishky.proxy.adapter.Base64IdEncoderDecoder;
import com.github.damianszwed.fishky.proxy.business.FlashcardFolderProviderFlow;
import com.github.damianszwed.fishky.proxy.business.FlashcardProviderFlow;
import com.github.damianszwed.fishky.proxy.port.IdEncoderDecoder;
import com.github.damianszwed.fishky.proxy.port.flashcard.EventSource;
import com.github.damianszwed.fishky.proxy.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardFolderStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BusinessConfiguration {

  @Bean
  FlashcardProviderFlow flashcardProviderFlow(
      FlashcardFolderStorage flashcardFolderStorage) {
    return new FlashcardProviderFlow(flashcardFolderStorage);
  }

  @Bean
  FlashcardFolderProviderFlow flashcardFolderProviderFlow(
      FlashcardFolderStorage flashcardFolderStorage) {
    return new FlashcardFolderProviderFlow(flashcardFolderStorage);
  }

  @Bean
  EventSource<Flashcard> eventSource(FlashcardProviderFlow flashcardProviderFlow) {
    return flashcardProviderFlow;
  }

  @Bean
  IdEncoderDecoder idEncoderDecoder() {
    return new Base64IdEncoderDecoder();
  }
}
