package com.github.damianszwed.fishky.flashcard.service.business;

import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import com.github.damianszwed.fishky.flashcard.service.adapter.storage.entity.FlashcardFolder;
import com.github.damianszwed.fishky.flashcard.service.port.CommandQueryHandler;
import com.github.damianszwed.fishky.flashcard.service.port.OwnerProvider;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardSearchService;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

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
        .zipWith(Mono.justOrEmpty(serverRequest.queryParam("q")))
        .flatMap(objects -> ok()
            .body(
                flashcardSearchService.search(objects.getT1(), objects.getT2()),
                FlashcardFolder.class))
        .switchIfEmpty(badRequest().build());
  }
}
