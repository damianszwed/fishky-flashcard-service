package com.github.damianszwed.fishky.proxy.configuration;

import com.github.damianszwed.fishky.proxy.adapter.storage.production.FlashcardMongoRepository;
import com.github.damianszwed.fishky.proxy.adapter.storage.production.FlashcardProductionStorage;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardProvider;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardRemover;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardSaver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfiguration {

  @Bean
  FlashcardProvider flashcardProvider(FlashcardMongoRepository flashcardMongoRepository) {
    return new FlashcardProductionStorage(flashcardMongoRepository);
  }

  @Bean
  FlashcardRemover flashcardRemover(FlashcardProductionStorage flashcardStorage) {
    return flashcardStorage;
  }

  @Bean
  FlashcardSaver flashcardSaver(FlashcardProductionStorage flashcardStorage) {
    return flashcardStorage;
  }
}
