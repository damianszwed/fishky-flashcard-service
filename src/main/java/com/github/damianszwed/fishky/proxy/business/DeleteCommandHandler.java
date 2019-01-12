package com.github.damianszwed.fishky.proxy.business;

import static org.springframework.web.reactive.function.server.ServerResponse.accepted;

import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardRemover;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class DeleteCommandHandler implements CommandQueryHandler {

  private FlashcardRemover flashcardRemover;

  public DeleteCommandHandler(FlashcardRemover flashcardRemover) {
    this.flashcardRemover = flashcardRemover;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    return Mono.fromSupplier(() -> Void.TYPE)
        .doOnNext((v) -> flashcardRemover.remove(serverRequest.pathVariable("id")))
        .flatMap(p -> accepted().build());
  }
}
