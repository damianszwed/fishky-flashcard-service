package com.github.damianszwed.fishky.proxy.configuration;

import com.github.damianszwed.fishky.proxy.adapter.security.ProductionOwnerProvider;
import com.github.damianszwed.fishky.proxy.adapter.storage.production.FlashcardFolderMongoRepository;
import com.github.damianszwed.fishky.proxy.adapter.storage.production.FlashcardFolderProductionStorage;
import com.github.damianszwed.fishky.proxy.port.OwnerProvider;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardFolderService;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.jwt.JwtDecoders;

@Configuration
@Profile("production")
public class ProductionSpecificConfiguration {

  @Bean
  FlashcardFolderService flashcardFolderStorage(
      FlashcardFolderMongoRepository flashcardFolderMongoRepository) {
    return new FlashcardFolderProductionStorage(flashcardFolderMongoRepository);
  }

  @Bean
  OwnerProvider ownerProvider(OAuth2ResourceServerProperties properties) {
    return new ProductionOwnerProvider(
        JwtDecoders.fromIssuerLocation(properties.getJwt().getIssuerUri()));
  }
}
