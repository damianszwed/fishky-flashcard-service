package com.github.damianszwed.fishky.flashcard.service.business;

import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import com.github.damianszwed.fishky.flashcard.service.port.CommandQueryHandler;
import com.github.damianszwed.fishky.flashcard.service.port.OwnerProvider;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardSearchService;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

public class FlashcardFolderSearchQueryHandler implements CommandQueryHandler {

  private final FlashcardSearchService flashcardSearchService;
  private final OwnerProvider ownerProvider;

  public FlashcardFolderSearchQueryHandler(
      FlashcardSearchService flashcardSearchService,
      OwnerProvider ownerProvider) {
    this.flashcardSearchService = flashcardSearchService;
    this.ownerProvider = ownerProvider;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    return Mono.justOrEmpty(ownerProvider.provide(serverRequest))
        .flatMap(ownerId -> Mono.justOrEmpty(serverRequest.queryParam("q"))
            .map(q -> Tuples.of(ownerId, q)))
        .flatMap(tuple -> ok().body(
            flashcardSearchService.search(tuple.getT1(), tuple.getT2()),
            Flashcard.class))
        .switchIfEmpty(badRequest().build());
  }
}
