package com.github.damianszwed.fishky.flashcard.service.configuration;

import com.github.damianszwed.fishky.flashcard.service.adapter.security.DevelopmentOwnerProvider;
import com.github.damianszwed.fishky.flashcard.service.adapter.storage.development.FlashcardFolderDevelopmentStorage;
import com.github.damianszwed.fishky.flashcard.service.port.OwnerProvider;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("development")
public class DevelopmentSpecificConfiguration {

  @Bean
  FlashcardFolderService flashcardFolderStorage() {
    return new FlashcardFolderDevelopmentStorage();
  }

  @Bean
  OwnerProvider ownerProvider() {
    return new DevelopmentOwnerProvider();
  }
}
