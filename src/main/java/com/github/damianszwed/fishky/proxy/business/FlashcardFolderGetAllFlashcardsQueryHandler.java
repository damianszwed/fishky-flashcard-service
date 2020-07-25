package com.github.damianszwed.fishky.proxy.business;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import com.github.damianszwed.fishky.proxy.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardFolder;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardFolderStorage;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class FlashcardFolderGetAllFlashcardsQueryHandler implements CommandQueryHandler {

  private FlashcardFolderStorage flashcardFolderStorage;

  public FlashcardFolderGetAllFlashcardsQueryHandler(
      FlashcardFolderStorage flashcardFolderStorage) {
    this.flashcardFolderStorage = flashcardFolderStorage;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    //TODO(Damian.Szwed) make sure that requester is an owner.
    return ok().body(
        flashcardFolderStorage.getById(serverRequest.pathVariable("id"))
            .map(FlashcardFolder::getFlashcards),
        Flashcard.class);
  }
}
