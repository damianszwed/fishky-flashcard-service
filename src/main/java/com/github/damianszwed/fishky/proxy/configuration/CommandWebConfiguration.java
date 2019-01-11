package com.github.damianszwed.fishky.proxy.configuration;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.resources;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.github.damianszwed.fishky.proxy.application.EventHandler;
import com.github.damianszwed.fishky.proxy.application.FlashcardProviderFlow;
import com.github.damianszwed.fishky.proxy.application.GetAllCommandHandler;
import com.github.damianszwed.fishky.proxy.application.GetAllQueryHandler;
import com.github.damianszwed.fishky.proxy.application.SwaggerHandler;
import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import com.github.damianszwed.fishky.proxy.port.flashcard.EventSource;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardProvider;
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
  public CommandQueryHandler getAllQueryHandler(FlashcardProvider flashcardProvider) {
    return new GetAllQueryHandler(flashcardProvider);
  }

  @Bean
  public CommandQueryHandler getAllCommandHandler(
      FlashcardProviderFlow flashcardProviderFlow) {
    return new GetAllCommandHandler(flashcardProviderFlow);
  }

  @Bean
  public CommandQueryHandler eventHandler(EventSource eventSource) {
    return new EventHandler(eventSource);
  }

  @Bean
  public CommandQueryHandler swaggerHandler() {
    return new SwaggerHandler();
  }

  @Bean
  public RouterFunction<ServerResponse> routes(
      CommandQueryHandler getAllQueryHandler,
      CommandQueryHandler getAllCommandHandler,
      CommandQueryHandler eventHandler,
      CommandQueryHandler swaggerHandler) {
    return route(GET("/flashcards"), getAllQueryHandler::handle)
        .andRoute(GET("/getAllFlashcardsCommand"), getAllCommandHandler::handle)
        .andRoute(GET("/flashcardsEventStream"), eventHandler::handle)
        .andRoute(GET("/"), swaggerHandler::handle)
        .and(resources("/**", new ClassPathResource("static/")));
  }
}
