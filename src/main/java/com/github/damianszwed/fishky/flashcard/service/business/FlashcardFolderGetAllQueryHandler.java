package com.github.damianszwed.fishky.flashcard.service.business;

import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import com.github.damianszwed.fishky.flashcard.service.adapter.storage.entity.FlashcardFolder;
import com.github.damianszwed.fishky.flashcard.service.adapter.web.resource.FlashcardFolderResource;
import com.github.damianszwed.fishky.flashcard.service.port.CommandQueryHandler;
import com.github.damianszwed.fishky.flashcard.service.port.OwnerProvider;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolderStorage;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class FlashcardFolderGetAllQueryHandler implements CommandQueryHandler {

  private final FlashcardFolderStorage flashcardFolderStorage;
  private final OwnerProvider ownerProvider;

  public FlashcardFolderGetAllQueryHandler(
      FlashcardFolderStorage flashcardFolderStorage,
      OwnerProvider ownerProvider) {
    this.flashcardFolderStorage = flashcardFolderStorage;
    this.ownerProvider = ownerProvider;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    return ownerProvider.provide(serverRequest)
        .map(flashcardFolderStorage::get)
        .map(flashcardFolderFlux -> flashcardFolderFlux.map(FlashcardFolder::toResource))
        .map(flashcardFolderFlux -> flashcardFolderFlux.map(
            FlashcardFolderGetAllQueryHandler::asOwner))
        .map(flashcardFolderFlux -> ok().body(flashcardFolderFlux, FlashcardFolderResource.class))
        .orElse(badRequest().build());
  }

  private static FlashcardFolderResource asOwner(FlashcardFolderResource flashcardFolder) {
    return flashcardFolder.toBuilder().isOwner(true).build();
  }
}
