package com.github.damianszwed.fishky.proxy.configuration;

import com.github.damianszwed.fishky.proxy.adapter.Base64IdEncoderDecoder;
import com.github.damianszwed.fishky.proxy.business.FlashcardFolderEmittingStorage;
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
      FlashcardFolderService flashcardFolderStorage) {
    return new FlashcardProviderFlow(flashcardFolderStorage);
  }

  @Bean
  EventSource<Flashcard> eventSource(FlashcardProviderFlow flashcardProviderFlow) {
    return flashcardProviderFlow;
  }

  @Bean
  FlashcardFolderProviderFlow flashcardFolderProviderFlow(
      FlashcardFolderService flashcardFolderStorage) {
    return new FlashcardFolderProviderFlow(flashcardFolderStorage);
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

  @Bean
  FlashcardFolderService flashcardFolderEmittingStorage(
      FlashcardFolderService flashcardFolderStorage,
      EventTrigger getAllFoldersEventTrigger) {
    return new FlashcardFolderEmittingStorage(flashcardFolderStorage, getAllFoldersEventTrigger);
  }
}
