package com.github.damianszwed.fishky.proxy.configuration;

import com.github.damianszwed.fishky.proxy.adapter.security.DevelopmentOwnerProvider;
import com.github.damianszwed.fishky.proxy.adapter.storage.development.FlashcardSetDevelopmentStorage;
import com.github.damianszwed.fishky.proxy.port.OwnerProvider;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardSetStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("development")
public class DevelopmentSpecificConfiguration {

  @Bean
  FlashcardSetStorage flashcardSetStorage() {
    return new FlashcardSetDevelopmentStorage();
  }

  @Bean
  OwnerProvider ownerProvider() {
    return new DevelopmentOwnerProvider();
  }
}
