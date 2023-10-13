package com.github.damianszwed.fishky.flashcard.service.configuration;

import com.github.damianszwed.fishky.flashcard.service.adapter.security.ProductionOwnerProvider;
import com.github.damianszwed.fishky.flashcard.service.port.OwnerProvider;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.reactive.CorsWebFilter;

@Configuration
@EnableWebFluxSecurity
@Profile("production")
public class SecurityConfiguration {

  @Bean
  SecurityWebFilterChain springSecurityFilterChain(
      final ServerHttpSecurity http,
      final CorsWebFilter corsWebFilter,
      final SecurityProperties securityProperties) {
    http
        .addFilterAt(corsWebFilter, SecurityWebFiltersOrder.CORS)
        .csrf(CsrfSpec::disable)
        .authorizeExchange(
            authorizeExchangeSpec -> authorizeExchangeSpec.pathMatchers("/healthCheck",
                    "/owners/" + securityProperties.getSystemUserExternalId() + "/flashcardFolders")
                .permitAll()
                .anyExchange()
                .authenticated())
        .oauth2ResourceServer(
            oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec.jwt(Customizer.withDefaults()));

    return http.build();
  }

  @Bean
  OwnerProvider ownerProvider(OAuth2ResourceServerProperties properties) {
    return new ProductionOwnerProvider(
        JwtDecoders.fromIssuerLocation(properties.getJwt().getIssuerUri()));
  }
}
