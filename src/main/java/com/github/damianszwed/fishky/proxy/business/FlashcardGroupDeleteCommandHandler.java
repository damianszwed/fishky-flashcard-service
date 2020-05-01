package com.github.damianszwed.fishky.proxy.business;

import static org.springframework.web.reactive.function.server.ServerResponse.accepted;

import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardGroupStorage;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class FlashcardGroupDeleteCommandHandler implements CommandQueryHandler {

  private FlashcardGroupStorage flashcardGroupStorage;

  public FlashcardGroupDeleteCommandHandler(FlashcardGroupStorage flashcardGroupStorage) {
    this.flashcardGroupStorage = flashcardGroupStorage;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    return Mono.fromSupplier(() -> Void.TYPE)
        .doOnNext((v) -> flashcardGroupStorage.remove(serverRequest.pathVariable("id")))
        .flatMap(p -> accepted().build());
  }
}
