package com.github.damianszwed.fishky.flashcard.service.configuration;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsFilterConfiguration {

  @Bean
  public CorsWebFilter corsWebFilter(SecurityProperties securityProperties) {
    CorsConfiguration corsConfig = new CorsConfiguration();
    corsConfig.setAllowedHeaders(securityProperties.getCors().getAllowedHeaders());
    corsConfig.setAllowedOriginPatterns(securityProperties.getCors().getAllowedOrigins());

    corsConfig
        .setAllowedMethods(
            Stream.of(HttpMethod.values()).map(HttpMethod::name).collect(Collectors.toList()));
    corsConfig.setAllowCredentials(securityProperties.getCors().getAllowCredentials());

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfig);

    return new CorsWebFilter(source);
  }
}
