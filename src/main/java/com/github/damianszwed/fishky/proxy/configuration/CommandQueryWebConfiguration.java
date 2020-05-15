package com.github.damianszwed.fishky.proxy.configuration;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.resources;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.github.damianszwed.fishky.proxy.business.FlashcardDeleteCommandHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardGetAllCommandHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardGetAllQueryHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardProviderFlow;
import com.github.damianszwed.fishky.proxy.business.FlashcardSaveCommandHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardServerSentEventHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardSetDeleteCommandHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardSetDeleteFlashcardCommandHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardSetGetAllFlashcardsQueryHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardSetGetAllQueryHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardSetSaveCommandHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardSetSaveFlashcardCommandHandler;
import com.github.damianszwed.fishky.proxy.business.SwaggerHandler;
import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import com.github.damianszwed.fishky.proxy.port.IdEncoderDecoder;
import com.github.damianszwed.fishky.proxy.port.OwnerProvider;
import com.github.damianszwed.fishky.proxy.port.flashcard.EventSource;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardSetStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;


@Configuration
public class CommandQueryWebConfiguration {

  @Bean
  public CommandQueryHandler flashcardGetAllQueryHandler(
      FlashcardSetStorage flashcardSetStorage) {
    return new FlashcardGetAllQueryHandler(flashcardSetStorage);
  }

  @Bean
  public CommandQueryHandler flashcardSaveCommandHandler(
      FlashcardSetStorage flashcardSetStorage,
      IdEncoderDecoder idEncoderDecoder) {
    return new FlashcardSaveCommandHandler(flashcardSetStorage, idEncoderDecoder);
  }

  @Bean
  public CommandQueryHandler flashcardDeleteCommandHandler(
      FlashcardSetStorage flashcardSetStorage) {
    return new FlashcardDeleteCommandHandler(flashcardSetStorage);
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
  public CommandQueryHandler flashcardSetGetAllQueryHandler(
      FlashcardSetStorage flashcardSetStorage, OwnerProvider ownerProvider) {
    return new FlashcardSetGetAllQueryHandler(flashcardSetStorage, ownerProvider);
  }

  @Bean
  public CommandQueryHandler flashcardSetSaveCommandHandler(
      FlashcardSetStorage flashcardSetStorage,
      IdEncoderDecoder idEncoderDecoder,
      OwnerProvider ownerProvider) {
    return new FlashcardSetSaveCommandHandler(flashcardSetStorage, idEncoderDecoder, ownerProvider);
  }

  @Bean
  public CommandQueryHandler flashcardSetDeleteCommandHandler(
      FlashcardSetStorage flashcardSetStorage) {
    return new FlashcardSetDeleteCommandHandler(flashcardSetStorage);
  }

  @Bean
  public CommandQueryHandler flashcardSetGetAllFlashcardsQueryHandler(
      FlashcardSetStorage flashcardSetStorage) {
    return new FlashcardSetGetAllFlashcardsQueryHandler(flashcardSetStorage);
  }

  @Bean
  public CommandQueryHandler flashcardSetSaveFlashcardCommandHandler(
      FlashcardSetStorage flashcardSetStorage,
      IdEncoderDecoder idEncoderDecoder,
      OwnerProvider ownerProvider) {
    return new FlashcardSetSaveFlashcardCommandHandler(
        flashcardSetStorage,
        idEncoderDecoder,
        ownerProvider);
  }

  @Bean
  public CommandQueryHandler flashcardSetDeleteFlashcardCommandHandler(
      FlashcardSetStorage flashcardSetStorage) {
    return new FlashcardSetDeleteFlashcardCommandHandler(flashcardSetStorage);
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
  public RouterFunction<ServerResponse> flashcardSetRoutes(
      CommandQueryHandler flashcardSetGetAllQueryHandler,
      CommandQueryHandler flashcardSetSaveCommandHandler,
      CommandQueryHandler flashcardSetDeleteCommandHandler,
      CommandQueryHandler flashcardSetGetAllFlashcardsQueryHandler,
      CommandQueryHandler flashcardSetSaveFlashcardCommandHandler,
      CommandQueryHandler flashcardSetDeleteFlashcardCommandHandler
  ) {
    return route(GET("/flashcardSets"), flashcardSetGetAllQueryHandler::handle)
        .andRoute(POST("/flashcardSets"), flashcardSetSaveCommandHandler::handle)
        .andRoute(DELETE("/flashcardSets/{id}"), flashcardSetDeleteCommandHandler::handle)
        .andRoute(GET("/flashcardSets/{id}/flashcards"),
            flashcardSetGetAllFlashcardsQueryHandler::handle)
        .andRoute(POST("/flashcardSets/{id}/flashcards"),
            flashcardSetSaveFlashcardCommandHandler::handle)
        .andRoute(DELETE("/flashcardSets/{flashcardSetId}/flashcards/{flashcardId}"),
            flashcardSetDeleteFlashcardCommandHandler::handle);
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
