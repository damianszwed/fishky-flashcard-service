package com.github.damianszwed.fishky.proxy.configuration;

import com.github.damianszwed.fishky.proxy.adapter.storage.production.FlashcardGroupMongoRepository;
import com.github.damianszwed.fishky.proxy.adapter.storage.production.FlashcardGroupProductionStorage;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardGroupStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("production")
public class ProductionStorageConfiguration {

  @Bean
  FlashcardGroupStorage flashcardGroupStorage(
      FlashcardGroupMongoRepository flashcardGroupMongoRepository) {
    return new FlashcardGroupProductionStorage(flashcardGroupMongoRepository);
  }
}
