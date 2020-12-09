package com.github.damianszwed.fishky.proxy.business;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import com.github.damianszwed.fishky.proxy.port.OwnerProvider;
import com.github.damianszwed.fishky.proxy.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardFolder;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardFolderService;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class FlashcardFolderGetAllFlashcardsQueryHandler implements CommandQueryHandler {

  private final FlashcardFolderService flashcardFolderService;
  private final OwnerProvider ownerProvider;

  public FlashcardFolderGetAllFlashcardsQueryHandler(
      FlashcardFolderService flashcardFolderService,
      OwnerProvider ownerProvider) {
    this.flashcardFolderService = flashcardFolderService;
    this.ownerProvider = ownerProvider;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    //TODO(Damian.Szwed) make sure that requester is an owner.
    return ok().body(
        flashcardFolderService
            .getById(ownerProvider.provide(serverRequest), serverRequest.pathVariable("id"))
            .map(FlashcardFolder::getFlashcards),
        Flashcard.class);
  }
}
