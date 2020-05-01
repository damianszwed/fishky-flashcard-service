package com.github.damianszwed.fishky.proxy.configuration;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.resources;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.github.damianszwed.fishky.proxy.business.FlashcardDeleteCommandHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardGetAllCommandHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardGetAllQueryHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardGroupDeleteCommandHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardGroupGetAllQueryHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardGroupSaveCommandHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardProviderFlow;
import com.github.damianszwed.fishky.proxy.business.FlashcardSaveCommandHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardServerSentEventHandler;
import com.github.damianszwed.fishky.proxy.business.SwaggerHandler;
import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import com.github.damianszwed.fishky.proxy.port.flashcard.EventSource;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardGroupStorage;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;


@Configuration
public class CommandQueryWebConfiguration {

  @Bean
  public CommandQueryHandler flashcardGetAllQueryHandler(FlashcardStorage flashcardStorage) {
    return new FlashcardGetAllQueryHandler(flashcardStorage);
  }

  @Bean
  public CommandQueryHandler flashcardSaveCommandHandler(FlashcardStorage flashcardStorage) {
    return new FlashcardSaveCommandHandler(flashcardStorage);
  }

  @Bean
  public CommandQueryHandler flashcardDeleteCommandHandler(FlashcardStorage flashcardStorage) {
    return new FlashcardDeleteCommandHandler(flashcardStorage);
  }

  @Bean
  public CommandQueryHandler flashcardGetAllCommandHandler(
      FlashcardProviderFlow flashcardProviderFlow) {
    return new FlashcardGetAllCommandHandler(flashcardProviderFlow);
  }

  @Bean
  public CommandQueryHandler flashcardServerSentEventHandler(EventSource eventSource) {
    return new FlashcardServerSentEventHandler(eventSource);
  }

  @Bean
  public CommandQueryHandler flashcardGroupGetAllQueryHandler(
      FlashcardGroupStorage flashcardGroupStorage) {
    return new FlashcardGroupGetAllQueryHandler(flashcardGroupStorage);
  }

  @Bean
  public CommandQueryHandler flashcardGroupSaveCommandHandler(
      FlashcardGroupStorage flashcardGroupStorage) {
    return new FlashcardGroupSaveCommandHandler(flashcardGroupStorage);
  }

  @Bean
  public CommandQueryHandler flashcardGroupDeleteCommandHandler(
      FlashcardGroupStorage flashcardGroupStorage) {
    return new FlashcardGroupDeleteCommandHandler(flashcardGroupStorage);
  }

  @Bean
  public CommandQueryHandler swaggerHandler() {
    return new SwaggerHandler();
  }

  @Bean
  public RouterFunction<ServerResponse> flashcardRoutes(
      CommandQueryHandler flashcardGetAllQueryHandler,
      CommandQueryHandler flashcardSaveCommandHandler,
      CommandQueryHandler flashcardDeleteCommandHandler) {
    return route(GET("/flashcards"), flashcardGetAllQueryHandler::handle)
        .andRoute(POST("/flashcards"), flashcardSaveCommandHandler::handle)
        .andRoute(DELETE("/flashcards/{id}"), flashcardDeleteCommandHandler::handle);
  }

  @Bean
  public RouterFunction<ServerResponse> flashcardGroupRoutes(
      CommandQueryHandler flashcardGroupGetAllQueryHandler,
      CommandQueryHandler flashcardGroupSaveCommandHandler,
      CommandQueryHandler flashcardGroupDeleteCommandHandler) {
    return route(GET("/flashcardGroups"), flashcardGroupGetAllQueryHandler::handle)
        .andRoute(POST("/flashcardGroups"), flashcardGroupSaveCommandHandler::handle)
        .andRoute(DELETE("/flashcardGroups/{id}"), flashcardGroupDeleteCommandHandler::handle);
  }

  @Bean
  public RouterFunction<ServerResponse> sseRoutes(
      CommandQueryHandler flashcardGetAllCommandHandler,
      CommandQueryHandler flashcardServerSentEventHandler) {
    return route(GET("/getAllFlashcardsCommand"), flashcardGetAllCommandHandler::handle)
        .andRoute(GET("/flashcardsEventStream"), flashcardServerSentEventHandler::handle);
  }

  @Bean
  RouterFunction<ServerResponse> routes(CommandQueryHandler swaggerHandler) {
    return route(GET("/"), swaggerHandler::handle)
        .and(resources("/**", new ClassPathResource("static/")));
  }
}
