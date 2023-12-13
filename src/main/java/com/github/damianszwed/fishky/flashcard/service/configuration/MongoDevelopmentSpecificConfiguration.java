package com.github.damianszwed.fishky.flashcard.service.configuration;

import com.github.damianszwed.fishky.flashcard.service.adapter.web.DevelopmentOwnerProvider;
import com.github.damianszwed.fishky.flashcard.service.port.OwnerProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"development", "mongo-development"})
public class MongoDevelopmentSpecificConfiguration {
  @Bean
  OwnerProvider ownerProvider() {
    return new DevelopmentOwnerProvider();
  }
}
