package com.github.damianszwed.fishky.proxy.configuration;

import com.github.damianszwed.fishky.proxy.adapter.storage.FlashcardStorageAdapter;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardGroupStorage;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlashcardStorageStorageConfiguration {

  @Bean
  FlashcardStorage flashcardStorage(FlashcardGroupStorage flashcardGroupStorage) {
    return new FlashcardStorageAdapter(flashcardGroupStorage);
  }
}
