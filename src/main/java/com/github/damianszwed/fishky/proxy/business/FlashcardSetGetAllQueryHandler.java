package com.github.damianszwed.fishky.proxy.business;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import com.github.damianszwed.fishky.proxy.port.OwnerProvider;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardSet;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardSetStorage;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class FlashcardSetGetAllQueryHandler implements CommandQueryHandler {

  private final FlashcardSetStorage flashcardSetStorage;
  private final OwnerProvider ownerProvider;

  public FlashcardSetGetAllQueryHandler(
      FlashcardSetStorage flashcardSetStorage,
      OwnerProvider ownerProvider) {
    this.flashcardSetStorage = flashcardSetStorage;
    this.ownerProvider = ownerProvider;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    return ok().body(
        flashcardSetStorage.get(ownerProvider.provide(serverRequest)),
        FlashcardSet.class);
  }
}
