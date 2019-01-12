package com.github.damianszwed.fishky.proxy.configuration;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.resources;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.github.damianszwed.fishky.proxy.business.DeleteCommandHandler;
import com.github.damianszwed.fishky.proxy.business.EventHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardProviderFlow;
import com.github.damianszwed.fishky.proxy.business.GetAllCommandHandler;
import com.github.damianszwed.fishky.proxy.business.GetAllQueryHandler;
import com.github.damianszwed.fishky.proxy.business.SaveCommandHandler;
import com.github.damianszwed.fishky.proxy.business.SwaggerHandler;
import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import com.github.damianszwed.fishky.proxy.port.flashcard.EventSource;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardProvider;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardRemover;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardSaver;
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
public class CommandQueryWebConfiguration {

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
  public CommandQueryHandler saveCommandHandler(
      FlashcardSaver flashcardSaver) {
    return new SaveCommandHandler(flashcardSaver);
  }

  @Bean
  public CommandQueryHandler deleteCommandHandler(
      FlashcardRemover flashcardRemover) {
    return new DeleteCommandHandler(flashcardRemover);
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
      CommandQueryHandler saveCommandHandler,
      CommandQueryHandler deleteCommandHandler,
      CommandQueryHandler getAllCommandHandler,
      CommandQueryHandler eventHandler,
      CommandQueryHandler swaggerHandler) {
    return route(GET("/flashcards"), getAllQueryHandler::handle)
        .andRoute(POST("/flashcards"), saveCommandHandler::handle)
        .andRoute(DELETE("/flashcards/{id}"), deleteCommandHandler::handle)
        .andRoute(GET("/getAllFlashcardsCommand"), getAllCommandHandler::handle)
        .andRoute(GET("/flashcardsEventStream"), eventHandler::handle)
        .andRoute(GET("/"), swaggerHandler::handle)
        .and(resources("/**", new ClassPathResource("static/")));
  }
}
