package com.github.damianszwed.fishky.flashcard.service.configuration;

import com.github.damianszwed.fishky.flashcard.service.adapter.Base64IdEncoderDecoder;
import com.github.damianszwed.fishky.flashcard.service.business.FlashcardFolderEmittingStorage;
import com.github.damianszwed.fishky.flashcard.service.business.FlashcardFolderProviderFlow;
import com.github.damianszwed.fishky.flashcard.service.port.EventTrigger;
import com.github.damianszwed.fishky.flashcard.service.port.IdEncoderDecoder;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.EventSource;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolder;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BusinessConfiguration {

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
