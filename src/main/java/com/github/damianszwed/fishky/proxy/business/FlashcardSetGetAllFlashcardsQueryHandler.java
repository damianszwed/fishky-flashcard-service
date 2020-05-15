package com.github.damianszwed.fishky.proxy.business;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import com.github.damianszwed.fishky.proxy.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardSet;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardSetStorage;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class FlashcardSetGetAllFlashcardsQueryHandler implements CommandQueryHandler {

  private FlashcardSetStorage flashcardSetStorage;

  public FlashcardSetGetAllFlashcardsQueryHandler(FlashcardSetStorage flashcardSetStorage) {
    this.flashcardSetStorage = flashcardSetStorage;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    //TODO(Damian.Szwed) make sure that requester is an owner.
    return ok().body(
        flashcardSetStorage.getById(serverRequest.pathVariable("id"))
            .map(FlashcardSet::getFlashcards),
        Flashcard.class);
  }
}
