package com.github.damianszwed.fishky.proxy.business;

import static org.springframework.web.reactive.function.server.ServerResponse.accepted;

import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardStorage;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class DeleteCommandHandler implements CommandQueryHandler {

  private FlashcardStorage flashcardStorage;

  public DeleteCommandHandler(FlashcardStorage flashcardStorage) {
    this.flashcardStorage = flashcardStorage;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    return Mono.fromSupplier(() -> Void.TYPE)
        .doOnNext((v) -> flashcardStorage.remove(serverRequest.pathVariable("id")))
        .flatMap(p -> accepted().build());
  }
}
