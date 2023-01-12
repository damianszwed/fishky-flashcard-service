package com.github.damianszwed.fishky.flashcard.service.business;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import com.github.damianszwed.fishky.flashcard.service.port.CommandQueryHandler;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardSearchService;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class FlashcardFolderReindexQueryHandler implements CommandQueryHandler {

  private final FlashcardSearchService flashcardSearchService;

  public FlashcardFolderReindexQueryHandler(FlashcardSearchService flashcardSearchService) {
    this.flashcardSearchService = flashcardSearchService;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    return flashcardSearchService.reindex()
        .flatMap(aBoolean -> {
          if (aBoolean) {
            return ok().build();
          } else {
            return ServerResponse.status(HttpStatus.FAILED_DEPENDENCY).build();
          }
        });
  }
}
