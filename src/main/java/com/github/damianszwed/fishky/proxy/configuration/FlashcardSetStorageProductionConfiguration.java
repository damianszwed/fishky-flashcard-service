package com.github.damianszwed.fishky.proxy.configuration;

import com.github.damianszwed.fishky.proxy.adapter.storage.production.FlashcardSetMongoRepository;
import com.github.damianszwed.fishky.proxy.adapter.storage.production.FlashcardSetProductionStorage;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardSetStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("production")
public class FlashcardSetStorageProductionConfiguration {

  @Bean
  FlashcardSetStorage flashcardSetStorage(
      FlashcardSetMongoRepository flashcardSetMongoRepository) {
    return new FlashcardSetProductionStorage(flashcardSetMongoRepository);
  }
}
