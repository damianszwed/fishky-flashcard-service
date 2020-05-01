package com.github.damianszwed.fishky.proxy.configuration;

import com.github.damianszwed.fishky.proxy.adapter.storage.FlashcardStorage;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardGroupStorage;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardProvider;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardRemover;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardSaver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfiguration {

  @Bean
  FlashcardStorage flashcardStorage(
      FlashcardGroupStorage flashcardGroupStorage) {
    return new FlashcardStorage(flashcardGroupStorage);
  }

  @Bean
  FlashcardProvider flashcardProvider(FlashcardStorage flashcardStorage) {
    return flashcardStorage;
  }

  @Bean
  FlashcardRemover flashcardRemover(FlashcardStorage flashcardStorage) {
    return flashcardStorage;
  }

  @Bean
  FlashcardSaver flashcardSaver(FlashcardStorage flashcardStorage) {
    return flashcardStorage;
  }
}
