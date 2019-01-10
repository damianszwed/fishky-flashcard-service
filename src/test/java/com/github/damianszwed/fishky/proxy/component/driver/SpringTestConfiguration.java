package com.github.damianszwed.fishky.proxy.component.driver;

import com.github.damianszwed.fishky.proxy.port.flashcard.EventSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.web.reactive.server.WebTestClient;


@Configuration
public class SpringTestConfiguration {

  @Bean
  FishkyProxyDriver patientServiceDriver(
      WebTestClient webTestClient,
      EventSource eventSource) {
    return new FishkyProxyDriver(webTestClient, eventSource);
  }

}
