package com.github.damianszwed.fishky.proxy.configuration;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.resources;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.github.damianszwed.fishky.proxy.application.FlashcardProviderFlow;
import com.github.damianszwed.fishky.proxy.application.GetAllCommandQuery;
import com.github.damianszwed.fishky.proxy.application.GetAllEvent;
import com.github.damianszwed.fishky.proxy.application.SwaggerCommandQuery;
import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import com.github.damianszwed.fishky.proxy.port.flashcard.EventSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurerComposite;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@EnableWebFlux
public class CommandWebConfiguration {

  @Bean
  public WebFluxConfigurer corsConfigurer() {
    return new WebFluxConfigurerComposite() {

      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*")
            .allowedMethods("*");
      }
    };
  }

  @Bean
  public CommandQueryHandler swaggerHandler() {
    return new SwaggerCommandQuery();
  }

  @Bean
  public CommandQueryHandler getAllCommand(
      FlashcardProviderFlow flashcardProviderFlow) {
    return new GetAllCommandQuery(flashcardProviderFlow);
  }

  @Bean
  public CommandQueryHandler getAllEvent(EventSource eventSource) {
    return new GetAllEvent(eventSource);
  }

  @Bean
  public RouterFunction<ServerResponse> routes(
      CommandQueryHandler getAllCommand,
      CommandQueryHandler swaggerHandler,
      CommandQueryHandler getAllEvent) {
    return route(GET("/flashcards"), getAllCommand::handle)
        .andRoute(GET("/flashcardsEventStream"), getAllEvent::handle)
        .andRoute(GET("/"), swaggerHandler::handle)
        .and(resources("/**", new ClassPathResource("static/")));
  }
}
