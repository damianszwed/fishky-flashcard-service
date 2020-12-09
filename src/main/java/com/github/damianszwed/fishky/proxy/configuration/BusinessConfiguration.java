package com.github.damianszwed.fishky.proxy.configuration;

import com.github.damianszwed.fishky.proxy.adapter.Base64IdEncoderDecoder;
import com.github.damianszwed.fishky.proxy.business.FlashcardFolderProviderFlow;
import com.github.damianszwed.fishky.proxy.business.FlashcardProviderFlow;
import com.github.damianszwed.fishky.proxy.port.EventTrigger;
import com.github.damianszwed.fishky.proxy.port.IdEncoderDecoder;
import com.github.damianszwed.fishky.proxy.port.flashcard.EventSource;
import com.github.damianszwed.fishky.proxy.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardFolder;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardFolderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BusinessConfiguration {

  @Bean
  FlashcardProviderFlow flashcardProviderFlow(
      FlashcardFolderService flashcardFolderService) {
    return new FlashcardProviderFlow(flashcardFolderService);
  }

  @Bean
  EventSource<Flashcard> eventSource(FlashcardProviderFlow flashcardProviderFlow) {
    return flashcardProviderFlow;
  }

  @Bean
  FlashcardFolderProviderFlow flashcardFolderProviderFlow(
      FlashcardFolderService flashcardFolderService) {
    return new FlashcardFolderProviderFlow(flashcardFolderService);
  }

  @Bean
  EventTrigger getAllFoldersEventTrigger(FlashcardFolderProviderFlow flashcardFolderProviderFlow) {
    return flashcardFolderProviderFlow;
  }

  @Bean
  EventSource<FlashcardFolder> flashcardFoldersEventSource(
      FlashcardFolderProviderFlow flashcardFolderProviderFlow) {
    return flashcardFolderProviderFlow;
  }

  @Bean
  IdEncoderDecoder idEncoderDecoder() {
    return new Base64IdEncoderDecoder();
  }
}
