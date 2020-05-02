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
import com.github.damianszwed.fishky.proxy.business.FlashcardGroupDeleteFlashcardCommandHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardGroupGetAllFlashcardsQueryHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardGroupGetAllQueryHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardGroupSaveCommandHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardGroupSaveFlashcardCommandHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardProviderFlow;
import com.github.damianszwed.fishky.proxy.business.FlashcardSaveCommandHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardServerSentEventHandler;
import com.github.damianszwed.fishky.proxy.business.SwaggerHandler;
import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import com.github.damianszwed.fishky.proxy.port.flashcard.EventSource;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardGroupStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;


@Configuration
public class CommandQueryWebConfiguration {

  @Bean
  public CommandQueryHandler flashcardGetAllQueryHandler(
      FlashcardGroupStorage flashcardGroupStorage) {
    return new FlashcardGetAllQueryHandler(flashcardGroupStorage);
  }

  @Bean
  public CommandQueryHandler flashcardSaveCommandHandler(
      FlashcardGroupStorage flashcardGroupStorage) {
    return new FlashcardSaveCommandHandler(flashcardGroupStorage);
  }

  @Bean
  public CommandQueryHandler flashcardDeleteCommandHandler(
      FlashcardGroupStorage flashcardGroupStorage) {
    return new FlashcardDeleteCommandHandler(flashcardGroupStorage);
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
  public CommandQueryHandler flashcardGroupGetAllFlashcardsQueryHandler(
      FlashcardGroupStorage flashcardGroupStorage) {
    return new FlashcardGroupGetAllFlashcardsQueryHandler(flashcardGroupStorage);
  }

  @Bean
  public CommandQueryHandler flashcardGroupSaveFlashcardCommandHandler(
      FlashcardGroupStorage flashcardGroupStorage) {
    return new FlashcardGroupSaveFlashcardCommandHandler(flashcardGroupStorage);
  }

  @Bean
  public CommandQueryHandler flashcardGroupDeleteFlashcardCommandHandler(
      FlashcardGroupStorage flashcardGroupStorage) {
    return new FlashcardGroupDeleteFlashcardCommandHandler(flashcardGroupStorage);
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
      CommandQueryHandler flashcardGroupDeleteCommandHandler,
      CommandQueryHandler flashcardGroupGetAllFlashcardsQueryHandler,
      CommandQueryHandler flashcardGroupSaveFlashcardCommandHandler,
      CommandQueryHandler flashcardGroupDeleteFlashcardCommandHandler
  ) {
    return route(GET("/flashcardGroups"), flashcardGroupGetAllQueryHandler::handle)
        .andRoute(POST("/flashcardGroups"), flashcardGroupSaveCommandHandler::handle)
        .andRoute(DELETE("/flashcardGroups/{id}"), flashcardGroupDeleteCommandHandler::handle)
        .andRoute(GET("/flashcardGroups/{id}/flashcards"),
            flashcardGroupGetAllFlashcardsQueryHandler::handle)
        .andRoute(POST("/flashcardGroups/{id}/flashcards"),
            flashcardGroupSaveFlashcardCommandHandler::handle)
        .andRoute(DELETE("/flashcardGroups/{flashcardGroupId}/flashcards/{flashcardId}"),
            flashcardGroupDeleteFlashcardCommandHandler::handle);
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
