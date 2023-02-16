package com.github.damianszwed.fishky.flashcard.service.configuration;

import com.github.damianszwed.fishky.flashcard.service.adapter.security.ProductionOwnerProvider;
import com.github.damianszwed.fishky.flashcard.service.port.OwnerProvider;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.reactive.CorsWebFilter;

@EnableWebFluxSecurity
@Profile("production")
public class SecurityConfiguration {

  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(
      final ServerHttpSecurity http,
      final CorsWebFilter corsWebFilter,
      final SecurityProperties securityProperties) {
    http
        .addFilterAt(corsWebFilter, SecurityWebFiltersOrder.CORS)
        .csrf().disable()
        .authorizeExchange()
        .pathMatchers(
            "/healthCheck",
            "/owners/" + securityProperties.getSystemUserExternalId() + "/flashcardFolders")
        .permitAll()
        .anyExchange()
        .authenticated()
        .and()
        .oauth2ResourceServer()
        .jwt();

    return http.build();
  }

  @Bean
  OwnerProvider ownerProvider(OAuth2ResourceServerProperties properties) {
    return new ProductionOwnerProvider(
        JwtDecoders.fromIssuerLocation(properties.getJwt().getIssuerUri()));
  }
}
