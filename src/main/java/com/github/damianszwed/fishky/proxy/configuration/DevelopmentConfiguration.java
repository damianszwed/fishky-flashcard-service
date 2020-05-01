package com.github.damianszwed.fishky.proxy.configuration;

import com.github.damianszwed.fishky.proxy.adapter.storage.development.FlashcardDevelopmentStorage;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardProvider;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardRemover;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardSaver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("development")
public class DevelopmentConfiguration {

  @Bean FlashcardDevelopmentStorage flashcardDevelopmentStorage() {
    return new FlashcardDevelopmentStorage();
  }

  @Bean
  FlashcardProvider flashcardProvider(FlashcardDevelopmentStorage flashcardDevelopmentStorage) {
    return flashcardDevelopmentStorage;
  }

  @Bean
  FlashcardRemover flashcardRemover(FlashcardDevelopmentStorage flashcardDevelopmentStorage) {
    return flashcardDevelopmentStorage;
  }

  @Bean
  FlashcardSaver flashcardSaver(FlashcardDevelopmentStorage flashcardDevelopmentStorage) {
    return flashcardDevelopmentStorage;
  }
}
