package com.github.damianszwed.fishky.flashcard.service.configuration;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.resources;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.github.damianszwed.fishky.flashcard.service.business.CopyFolderCommandHandler;
import com.github.damianszwed.fishky.flashcard.service.business.FlashcardFolderDeleteCommandHandler;
import com.github.damianszwed.fishky.flashcard.service.business.FlashcardFolderDeleteFlashcardCommandHandler;
import com.github.damianszwed.fishky.flashcard.service.business.FlashcardFolderGetAllCommandHandler;
import com.github.damianszwed.fishky.flashcard.service.business.FlashcardFolderGetAllFlashcardsQueryHandler;
import com.github.damianszwed.fishky.flashcard.service.business.FlashcardFolderGetAllQueryHandler;
import com.github.damianszwed.fishky.flashcard.service.business.FlashcardFolderModifyFlashcardCommandHandler;
import com.github.damianszwed.fishky.flashcard.service.business.FlashcardFolderProviderFlow;
import com.github.damianszwed.fishky.flashcard.service.business.FlashcardFolderReindexQueryHandler;
import com.github.damianszwed.fishky.flashcard.service.business.FlashcardFolderSaveCommandHandler;
import com.github.damianszwed.fishky.flashcard.service.business.FlashcardFolderSaveFlashcardCommandHandler;
import com.github.damianszwed.fishky.flashcard.service.business.FlashcardFolderSearchQueryHandler;
import com.github.damianszwed.fishky.flashcard.service.business.FlashcardFolderServerSentEventHandler;
import com.github.damianszwed.fishky.flashcard.service.business.HealthCheckHandler;
import com.github.damianszwed.fishky.flashcard.service.business.OwnersFoldersGetAllQueryHandler;
import com.github.damianszwed.fishky.flashcard.service.business.SwaggerHandler;
import com.github.damianszwed.fishky.flashcard.service.business.ValidatorRequestHandler;
import com.github.damianszwed.fishky.flashcard.service.port.CommandQueryHandler;
import com.github.damianszwed.fishky.flashcard.service.port.IdEncoderDecoder;
import com.github.damianszwed.fishky.flashcard.service.port.OwnerProvider;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.EventSource;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolder;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolderService;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardSearchService;
import jakarta.validation.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;


@Configuration
public class CommandQueryWebConfiguration {

  @Bean
  public ValidatorRequestHandler validatorRequestHandler(Validator validator) {
    return new ValidatorRequestHandler(validator);
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
  public CommandQueryHandler flashcardFolderSearchQueryHandler(
      FlashcardSearchService flashcardSearchService, OwnerProvider ownerProvider) {
    return new FlashcardFolderSearchQueryHandler(flashcardSearchService, ownerProvider);
  }

  @Bean
  public CommandQueryHandler flashcardFolderReindexQueryHandler(
      FlashcardSearchService flashcardSearchService) {
    return new FlashcardFolderReindexQueryHandler(flashcardSearchService);
  }

  @Bean
  public CommandQueryHandler ownersFoldersGetAllQueryHandler(
      SecurityProperties securityProperties,
      FlashcardFolderService flashcardFolderEmittingStorage,
      OwnerProvider ownerProvider) {
    return new OwnersFoldersGetAllQueryHandler(securityProperties, flashcardFolderEmittingStorage,
        ownerProvider);
  }

  @Bean
  public CommandQueryHandler copyFolderCommandHandler(
      SecurityProperties securityProperties,
      FlashcardFolderService flashcardFolderEmittingStorage,
      IdEncoderDecoder idEncoderDecoder,
      OwnerProvider ownerProvider) {
    return new CopyFolderCommandHandler(
        securityProperties,
        flashcardFolderEmittingStorage,
        idEncoderDecoder,
        ownerProvider);
  }

  @Bean
  public CommandQueryHandler flashcardFolderSaveCommandHandler(
      FlashcardFolderService flashcardFolderEmittingStorage,
      IdEncoderDecoder idEncoderDecoder,
      OwnerProvider ownerProvider,
      ValidatorRequestHandler validatorRequestHandler) {
    return new FlashcardFolderSaveCommandHandler(flashcardFolderEmittingStorage, idEncoderDecoder,
        ownerProvider, validatorRequestHandler);
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
  public RouterFunction<ServerResponse> ownerRoutes(
      CommandQueryHandler ownersFoldersGetAllQueryHandler,
      CommandQueryHandler copyFolderCommandHandler) {
    return route(GET("/owners/{ownerId}/flashcardFolders"), ownersFoldersGetAllQueryHandler::handle)
        .andRoute(POST("/owners/{ownerId}/flashcardFolders/{folderId}/copy"),
            copyFolderCommandHandler::handle);
  }

  @Bean
  public RouterFunction<ServerResponse> flashcardFolderRoutes(
      CommandQueryHandler flashcardFolderGetAllQueryHandler,
      CommandQueryHandler flashcardFolderSearchQueryHandler,
      CommandQueryHandler flashcardFolderReindexQueryHandler,
      CommandQueryHandler flashcardFolderSaveCommandHandler,
      CommandQueryHandler flashcardFolderDeleteCommandHandler,
      CommandQueryHandler flashcardFolderGetAllFlashcardsQueryHandler,
      CommandQueryHandler flashcardFolderSaveFlashcardCommandHandler,
      CommandQueryHandler flashcardFolderModifyFlashcardCommandHandler,
      CommandQueryHandler flashcardFolderDeleteFlashcardCommandHandler) {
    return route(GET("/flashcardFolders"), flashcardFolderGetAllQueryHandler::handle)
        .andRoute(GET("/flashcardFolders/search"), flashcardFolderSearchQueryHandler::handle)
        .andRoute(POST("/flashcardFolders/reindex"), flashcardFolderReindexQueryHandler::handle)
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
      CommandQueryHandler flashcardFolderGetAllCommandHandler,
      CommandQueryHandler flashcardFolderServerSentEventHandler) {
    return route(GET("/getAllFlashcardFoldersCommand"),
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
