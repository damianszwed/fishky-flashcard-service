package com.github.damianszwed.fishky.proxy.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.reactive.CorsWebFilter;

@EnableWebFluxSecurity
@Profile("production")
public class SecurityConfiguration {

  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(
      final ServerHttpSecurity http,
      final CorsWebFilter corsWebFilter) {
    http
        .addFilterAt(corsWebFilter, SecurityWebFiltersOrder.CORS)
        .csrf().disable()
        .authorizeExchange()
        .pathMatchers("/management/**").permitAll()
        .anyExchange()
        .authenticated()
        .and()
        .oauth2ResourceServer()
        .jwt();

    return http.build();
  }
}
