package com.github.damianszwed.fishky.flashcard.service.business;

import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import com.github.damianszwed.fishky.flashcard.service.adapter.storage.entity.Flashcard;
import com.github.damianszwed.fishky.flashcard.service.adapter.storage.entity.FlashcardFolder;
import com.github.damianszwed.fishky.flashcard.service.port.CommandQueryHandler;
import com.github.damianszwed.fishky.flashcard.service.port.OwnerProvider;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolderStorage;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class FlashcardFolderGetAllFlashcardsQueryHandler implements CommandQueryHandler {

  private final FlashcardFolderStorage flashcardFolderStorage;
  private final OwnerProvider ownerProvider;

  public FlashcardFolderGetAllFlashcardsQueryHandler(
      FlashcardFolderStorage flashcardFolderStorage,
      OwnerProvider ownerProvider) {
    this.flashcardFolderStorage = flashcardFolderStorage;
    this.ownerProvider = ownerProvider;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    //TODO(Damian.Szwed) make sure that requester is an owner.
    return Mono.justOrEmpty(ownerProvider.provide(serverRequest))
        .flatMap(ownerId -> ok().body(
            flashcardFolderStorage
                .getById(ownerId, serverRequest.pathVariable("id"))
                .map(FlashcardFolder::getFlashcards),
            Flashcard.class))
        .switchIfEmpty(badRequest().build());
  }
}
