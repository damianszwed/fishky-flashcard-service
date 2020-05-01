package com.github.damianszwed.fishky.proxy.configuration;

import com.github.damianszwed.fishky.proxy.adapter.storage.development.FlashcardGroupDevelopmentStorage;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardGroupStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("development")
public class FlashcardGroupStorageDevelopmentConfiguration {

  @Bean
  FlashcardGroupStorage flashcardGroupStorage() {
    return new FlashcardGroupDevelopmentStorage();
  }
}
