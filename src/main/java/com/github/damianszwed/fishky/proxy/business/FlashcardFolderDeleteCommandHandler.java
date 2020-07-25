package com.github.damianszwed.fishky.proxy.business;

import static org.springframework.web.reactive.function.server.ServerResponse.accepted;

import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardFolderStorage;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class FlashcardFolderDeleteCommandHandler implements CommandQueryHandler {

  private FlashcardFolderStorage flashcardFolderStorage;

  public FlashcardFolderDeleteCommandHandler(FlashcardFolderStorage flashcardFolderStorage) {
    this.flashcardFolderStorage = flashcardFolderStorage;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    //TODO(Damian.Szwed) Make sure that only owner is able to delete it's flashcard folder.
    return Mono.fromSupplier(() -> Void.TYPE)
        .doOnNext((v) -> flashcardFolderStorage.remove(serverRequest.pathVariable("id")))
        .flatMap(p -> accepted().build());
  }
}
