package com.github.damianszwed.fishky.flashcard.service.configuration;

import com.github.damianszwed.fishky.flashcard.service.adapter.storage.production.FlashcardFolderMongoRepository;
import com.github.damianszwed.fishky.flashcard.service.adapter.storage.production.MongoFlashcardSearchService;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardSearchService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!elasticsearch")
public class MongoSearchConfiguration {

  @Bean
  FlashcardSearchService flashcardSearchService(
      FlashcardFolderMongoRepository flashcardFolderMongoRepository) {
    return new MongoFlashcardSearchService(flashcardFolderMongoRepository);
  }
}
