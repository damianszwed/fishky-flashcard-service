package com.github.damianszwed.fishky.proxy.configuration;

import com.github.damianszwed.fishky.proxy.adapter.storage.production.FlashcardMongoRepository;
import com.github.damianszwed.fishky.proxy.adapter.storage.production.FlashcardProductionStorage;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardProvider;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardRemover;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardSaver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("production")
public class StorageConfiguration {

  @Bean
  FlashcardProductionStorage flashcardProductionStorage(
      FlashcardMongoRepository flashcardMongoRepository) {
    return new FlashcardProductionStorage(flashcardMongoRepository);
  }

  @Bean
  FlashcardProvider flashcardProvider(FlashcardProductionStorage flashcardProductionStorage) {
    return flashcardProductionStorage;
  }

  @Bean
  FlashcardRemover flashcardRemover(FlashcardProductionStorage flashcardProductionStorage) {
    return flashcardProductionStorage;
  }

  @Bean
  FlashcardSaver flashcardSaver(FlashcardProductionStorage flashcardProductionStorage) {
    return flashcardProductionStorage;
  }
}
