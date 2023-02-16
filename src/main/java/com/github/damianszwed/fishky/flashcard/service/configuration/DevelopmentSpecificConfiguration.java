package com.github.damianszwed.fishky.flashcard.service.configuration;

import com.github.damianszwed.fishky.flashcard.service.adapter.security.DevelopmentOwnerProvider;
import com.github.damianszwed.fishky.flashcard.service.adapter.storage.development.FlashcardFolderDevelopmentStorage;
import com.github.damianszwed.fishky.flashcard.service.port.OwnerProvider;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"development", "mongo-development"})//TODO(Damian.Szwed)After search dev remove mongo-development entry
public class DevelopmentSpecificConfiguration {

  @Bean
  @Profile("!mongo-development")//TODO(Damian.Szwed)After search dev remove this line.
  FlashcardFolderService flashcardFolderStorage() {
    return new FlashcardFolderDevelopmentStorage();
  }

  @Bean
  OwnerProvider ownerProvider() {
    return new DevelopmentOwnerProvider();
  }
}
