package com.github.damianszwed.fishky.proxy.business;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardSet;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardSetStorage;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class FlashcardSetGetAllQueryHandler implements CommandQueryHandler {

  private FlashcardSetStorage flashcardSetStorage;

  public FlashcardSetGetAllQueryHandler(FlashcardSetStorage flashcardSetStorage) {
    this.flashcardSetStorage = flashcardSetStorage;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    return ok().body(
        flashcardSetStorage.get("user1@example.com"),
        FlashcardSet.class);
  }
}
