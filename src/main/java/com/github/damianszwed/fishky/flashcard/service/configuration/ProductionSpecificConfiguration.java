package com.github.damianszwed.fishky.flashcard.service.configuration;

import com.github.damianszwed.fishky.flashcard.service.adapter.storage.production.FlashcardFolderMongoRepository;
import com.github.damianszwed.fishky.flashcard.service.adapter.storage.production.FlashcardFolderProductionStorage;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"production", "mongo-development"})
//TODO(Damian.Szwed)After search dev remove mongo-development entry
public class ProductionSpecificConfiguration {

  @Bean
  FlashcardFolderService flashcardFolderStorage(
      FlashcardFolderMongoRepository flashcardFolderMongoRepository) {
    return new FlashcardFolderProductionStorage(flashcardFolderMongoRepository);
  }

}
