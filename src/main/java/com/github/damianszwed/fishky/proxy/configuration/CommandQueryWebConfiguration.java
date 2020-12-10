package com.github.damianszwed.fishky.proxy.configuration;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.resources;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.github.damianszwed.fishky.proxy.business.FlashcardFolderDeleteCommandHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardFolderDeleteFlashcardCommandHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardFolderGetAllCommandHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardFolderGetAllFlashcardsQueryHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardFolderGetAllQueryHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardFolderModifyFlashcardCommandHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardFolderProviderFlow;
import com.github.damianszwed.fishky.proxy.business.FlashcardFolderSaveCommandHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardFolderSaveFlashcardCommandHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardFolderServerSentEventHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardGetAllCommandHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardProviderFlow;
import com.github.damianszwed.fishky.proxy.business.FlashcardServerSentEventHandler;
import com.github.damianszwed.fishky.proxy.business.HealthCheckHandler;
import com.github.damianszwed.fishky.proxy.business.SwaggerHandler;
import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import com.github.damianszwed.fishky.proxy.port.IdEncoderDecoder;
import com.github.damianszwed.fishky.proxy.port.OwnerProvider;
import com.github.damianszwed.fishky.proxy.port.flashcard.EventSource;
import com.github.damianszwed.fishky.proxy.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardFolder;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardFolderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;


@Configuration
public class CommandQueryWebConfiguration {

  @Bean
  public CommandQueryHandler flashcardGetAllCommandHandler(
      FlashcardProviderFlow flashcardProviderFlow) {
    return new FlashcardGetAllCommandHandler(flashcardProviderFlow);
  }

  @Bean
  public CommandQueryHandler flashcardServerSentEventHandler(EventSource<Flashcard> eventSource) {
    return new FlashcardServerSentEventHandler(eventSource);
  }

  @Bean
  public CommandQueryHandler flashcardFolderGetAllCommandHandler(
      FlashcardFolderProviderFlow flashcardFolderProviderFlow,
      OwnerProvider ownerProvider) {
    return new FlashcardFolderGetAllCommandHandler(flashcardFolderProviderFlow, ownerProvider);
  }

  @Bean
  public CommandQueryHandler flashcardFolderServerSentEventHandler(
      EventSource<FlashcardFolder> flashcardFoldersEventSource,
      OwnerProvider ownerProvider) {
    return new FlashcardFolderServerSentEventHandler(flashcardFoldersEventSource, ownerProvider);
  }

  @Bean
  public CommandQueryHandler flashcardFolderGetAllQueryHandler(
      FlashcardFolderService flashcardFolderEmittingStorage, OwnerProvider ownerProvider) {
    return new FlashcardFolderGetAllQueryHandler(flashcardFolderEmittingStorage, ownerProvider);
  }

  @Bean
  public CommandQueryHandler flashcardFolderSaveCommandHandler(
      FlashcardFolderService flashcardFolderEmittingStorage,
      IdEncoderDecoder idEncoderDecoder,
      OwnerProvider ownerProvider) {
    return new FlashcardFolderSaveCommandHandler(flashcardFolderEmittingStorage, idEncoderDecoder,
        ownerProvider);
  }

  @Bean
  public CommandQueryHandler flashcardFolderDeleteCommandHandler(
      FlashcardFolderService flashcardFolderEmittingStorage,
      OwnerProvider ownerProvider) {
    return new FlashcardFolderDeleteCommandHandler(flashcardFolderEmittingStorage, ownerProvider);
  }

  @Bean
  public CommandQueryHandler flashcardFolderGetAllFlashcardsQueryHandler(
      FlashcardFolderService flashcardFolderEmittingStorage,
      OwnerProvider ownerProvider) {
    return new FlashcardFolderGetAllFlashcardsQueryHandler(
        flashcardFolderEmittingStorage,
        ownerProvider);
  }

  @Bean
  public CommandQueryHandler flashcardFolderSaveFlashcardCommandHandler(
      FlashcardFolderService flashcardFolderEmittingStorage,
      IdEncoderDecoder idEncoderDecoder,
      OwnerProvider ownerProvider) {
    return new FlashcardFolderSaveFlashcardCommandHandler(
        flashcardFolderEmittingStorage,
        idEncoderDecoder,
        ownerProvider);
  }

  @Bean
  public CommandQueryHandler flashcardFolderModifyFlashcardCommandHandler(
      FlashcardFolderService flashcardFolderEmittingStorage,
      IdEncoderDecoder idEncoderDecoder,
      OwnerProvider ownerProvider) {
    return new FlashcardFolderModifyFlashcardCommandHandler(
        flashcardFolderEmittingStorage,
        idEncoderDecoder,
        ownerProvider);
  }

  @Bean
  public CommandQueryHandler flashcardFolderDeleteFlashcardCommandHandler(
      FlashcardFolderService flashcardFolderEmittingStorage,
      OwnerProvider ownerProvider) {
    return new FlashcardFolderDeleteFlashcardCommandHandler(
        flashcardFolderEmittingStorage,
        ownerProvider);
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
  public RouterFunction<ServerResponse> flashcardFolderRoutes(
      CommandQueryHandler flashcardFolderGetAllQueryHandler,
      CommandQueryHandler flashcardFolderSaveCommandHandler,
      CommandQueryHandler flashcardFolderDeleteCommandHandler,
      CommandQueryHandler flashcardFolderGetAllFlashcardsQueryHandler,
      CommandQueryHandler flashcardFolderSaveFlashcardCommandHandler,
      CommandQueryHandler flashcardFolderModifyFlashcardCommandHandler,
      CommandQueryHandler flashcardFolderDeleteFlashcardCommandHandler
  ) {
    return route(GET("/flashcardFolders"), flashcardFolderGetAllQueryHandler::handle)
        .andRoute(POST("/flashcardFolders"), flashcardFolderSaveCommandHandler::handle)
        .andRoute(DELETE("/flashcardFolders/{id}"), flashcardFolderDeleteCommandHandler::handle)
        .andRoute(GET("/flashcardFolders/{id}/flashcards"),
            flashcardFolderGetAllFlashcardsQueryHandler::handle)
        .andRoute(POST("/flashcardFolders/{id}/flashcards"),
            flashcardFolderSaveFlashcardCommandHandler::handle)
        .andRoute(PUT("/flashcardFolders/{id}/flashcards"),
            flashcardFolderModifyFlashcardCommandHandler::handle)
        .andRoute(DELETE("/flashcardFolders/{flashcardFolderId}/flashcards/{flashcardId}"),
            flashcardFolderDeleteFlashcardCommandHandler::handle);
  }

  @Bean
  public RouterFunction<ServerResponse> sseRoutes(
      CommandQueryHandler flashcardGetAllCommandHandler,
      CommandQueryHandler flashcardServerSentEventHandler,
      CommandQueryHandler flashcardFolderGetAllCommandHandler,
      CommandQueryHandler flashcardFolderServerSentEventHandler) {
    return route(GET("/getAllFlashcardsCommand"), flashcardGetAllCommandHandler::handle)
        .andRoute(GET("/flashcardsEventStream"), flashcardServerSentEventHandler::handle)
        .andRoute(GET("/getAllFlashcardFoldersCommand"),
            flashcardFolderGetAllCommandHandler::handle)
        .andRoute(GET("/flashcardFoldersEventStream"),
            flashcardFolderServerSentEventHandler::handle);
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
