package com.github.damianszwed.fishky.proxy.configuration;

import com.github.damianszwed.fishky.proxy.adapter.security.ProductionOwnerProvider;
import com.github.damianszwed.fishky.proxy.adapter.storage.production.FlashcardSetMongoRepository;
import com.github.damianszwed.fishky.proxy.adapter.storage.production.FlashcardSetProductionStorage;
import com.github.damianszwed.fishky.proxy.port.OwnerProvider;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardSetStorage;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.jwt.JwtDecoders;

@Configuration
@Profile("production")
public class ProductionSpecificConfiguration {

  @Bean
  FlashcardSetStorage flashcardSetStorage(
      FlashcardSetMongoRepository flashcardSetMongoRepository) {
    return new FlashcardSetProductionStorage(flashcardSetMongoRepository);
  }

  @Bean
  OwnerProvider ownerProvider(OAuth2ResourceServerProperties properties) {
    return new ProductionOwnerProvider(
        JwtDecoders.fromIssuerLocation(properties.getJwt().getIssuerUri()));
  }
}
