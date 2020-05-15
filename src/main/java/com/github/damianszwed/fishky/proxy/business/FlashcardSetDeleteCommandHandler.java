package com.github.damianszwed.fishky.proxy.business;

import static org.springframework.web.reactive.function.server.ServerResponse.accepted;

import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardSetStorage;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class FlashcardSetDeleteCommandHandler implements CommandQueryHandler {

  private FlashcardSetStorage flashcardSetStorage;

  public FlashcardSetDeleteCommandHandler(FlashcardSetStorage flashcardSetStorage) {
    this.flashcardSetStorage = flashcardSetStorage;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    //TODO(Damian.Szwed) Make sure that only owner is able to delete it's flashcard set.
    return Mono.fromSupplier(() -> Void.TYPE)
        .doOnNext((v) -> flashcardSetStorage.remove(serverRequest.pathVariable("id")))
        .flatMap(p -> accepted().build());
  }
}
