package com.github.damianszwed.fishky.proxy.configuration;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.resources;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.github.damianszwed.fishky.proxy.business.FlashcardDeleteCommandHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardFolderDeleteCommandHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardFolderDeleteFlashcardCommandHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardFolderGetAllFlashcardsQueryHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardFolderGetAllQueryHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardFolderSaveCommandHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardFolderSaveFlashcardCommandHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardGetAllCommandHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardGetAllQueryHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardProviderFlow;
import com.github.damianszwed.fishky.proxy.business.FlashcardSaveCommandHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardServerSentEventHandler;
import com.github.damianszwed.fishky.proxy.business.HealthCheckHandler;
import com.github.damianszwed.fishky.proxy.business.SwaggerHandler;
import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import com.github.damianszwed.fishky.proxy.port.IdEncoderDecoder;
import com.github.damianszwed.fishky.proxy.port.OwnerProvider;
import com.github.damianszwed.fishky.proxy.port.flashcard.EventSource;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardFolderStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;


@Configuration
public class CommandQueryWebConfiguration {

  @Bean
  public CommandQueryHandler flashcardGetAllQueryHandler(
      FlashcardFolderStorage flashcardFolderStorage) {
    return new FlashcardGetAllQueryHandler(flashcardFolderStorage);
  }

  @Bean
  public CommandQueryHandler flashcardSaveCommandHandler(
      FlashcardFolderStorage flashcardFolderStorage,
      IdEncoderDecoder idEncoderDecoder) {
    return new FlashcardSaveCommandHandler(flashcardFolderStorage, idEncoderDecoder);
  }

  @Bean
  public CommandQueryHandler flashcardDeleteCommandHandler(
      FlashcardFolderStorage flashcardFolderStorage) {
    return new FlashcardDeleteCommandHandler(flashcardFolderStorage);
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
  public CommandQueryHandler flashcardFolderGetAllQueryHandler(
      FlashcardFolderStorage flashcardFolderStorage, OwnerProvider ownerProvider) {
    return new FlashcardFolderGetAllQueryHandler(flashcardFolderStorage, ownerProvider);
  }

  @Bean
  public CommandQueryHandler flashcardFolderSaveCommandHandler(
      FlashcardFolderStorage flashcardFolderStorage,
      IdEncoderDecoder idEncoderDecoder,
      OwnerProvider ownerProvider) {
    return new FlashcardFolderSaveCommandHandler(flashcardFolderStorage, idEncoderDecoder,
        ownerProvider);
  }

  @Bean
  public CommandQueryHandler flashcardFolderDeleteCommandHandler(
      FlashcardFolderStorage flashcardFolderStorage) {
    return new FlashcardFolderDeleteCommandHandler(flashcardFolderStorage);
  }

  @Bean
  public CommandQueryHandler flashcardFolderGetAllFlashcardsQueryHandler(
      FlashcardFolderStorage flashcardFolderStorage) {
    return new FlashcardFolderGetAllFlashcardsQueryHandler(flashcardFolderStorage);
  }

  @Bean
  public CommandQueryHandler flashcardFolderSaveFlashcardCommandHandler(
      FlashcardFolderStorage flashcardFolderStorage,
      IdEncoderDecoder idEncoderDecoder,
      OwnerProvider ownerProvider) {
    return new FlashcardFolderSaveFlashcardCommandHandler(
        flashcardFolderStorage,
        idEncoderDecoder,
        ownerProvider);
  }

  @Bean
  public CommandQueryHandler flashcardFolderDeleteFlashcardCommandHandler(
      FlashcardFolderStorage flashcardFolderStorage) {
    return new FlashcardFolderDeleteFlashcardCommandHandler(flashcardFolderStorage);
  }

  @Bean
  public CommandQueryHandler healthCheckHandler() {
    return new HealthCheckHandler();
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
  public RouterFunction<ServerResponse> flashcardFolderRoutes(
      CommandQueryHandler flashcardFolderGetAllQueryHandler,
      CommandQueryHandler flashcardFolderSaveCommandHandler,
      CommandQueryHandler flashcardFolderDeleteCommandHandler,
      CommandQueryHandler flashcardFolderGetAllFlashcardsQueryHandler,
      CommandQueryHandler flashcardFolderSaveFlashcardCommandHandler,
      CommandQueryHandler flashcardFolderDeleteFlashcardCommandHandler
  ) {
    return route(GET("/flashcardFolders"), flashcardFolderGetAllQueryHandler::handle)
        .andRoute(POST("/flashcardFolders"), flashcardFolderSaveCommandHandler::handle)
        .andRoute(DELETE("/flashcardFolders/{id}"), flashcardFolderDeleteCommandHandler::handle)
        .andRoute(GET("/flashcardFolders/{id}/flashcards"),
            flashcardFolderGetAllFlashcardsQueryHandler::handle)
        .andRoute(POST("/flashcardFolders/{id}/flashcards"),
            flashcardFolderSaveFlashcardCommandHandler::handle)
        .andRoute(DELETE("/flashcardFolders/{flashcardFolderId}/flashcards/{flashcardId}"),
            flashcardFolderDeleteFlashcardCommandHandler::handle);
  }

  @Bean
  public RouterFunction<ServerResponse> sseRoutes(
      CommandQueryHandler flashcardGetAllCommandHandler,
      CommandQueryHandler flashcardServerSentEventHandler) {
    return route(GET("/getAllFlashcardsCommand"), flashcardGetAllCommandHandler::handle)
        .andRoute(GET("/flashcardsEventStream"), flashcardServerSentEventHandler::handle);
  }

  @Bean
  RouterFunction<ServerResponse> routes(
      CommandQueryHandler swaggerHandler,
      CommandQueryHandler healthCheckHandler) {
    return route(GET("/"), swaggerHandler::handle)
        .andRoute(GET("/healthCheck"), healthCheckHandler::handle)
        .and(resources("/**", new ClassPathResource("static/")));
  }
}
