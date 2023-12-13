package com.github.damianszwed.fishky.flashcard.service.configuration;

import com.github.damianszwed.fishky.flashcard.service.adapter.storage.mongo.FlashcardFolderMongoRepository;
import com.github.damianszwed.fishky.flashcard.service.adapter.storage.mongo.FlashcardFolderProductionStorage;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolderStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"production", "mongo-development"})
public class ProductionSpecificConfiguration {

  @Bean
  FlashcardFolderStorage flashcardFolderStorage(
      FlashcardFolderMongoRepository flashcardFolderMongoRepository) {
    return new FlashcardFolderProductionStorage(flashcardFolderMongoRepository);
  }

}
