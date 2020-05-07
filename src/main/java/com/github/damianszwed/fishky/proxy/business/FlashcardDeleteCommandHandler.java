package com.github.damianszwed.fishky.proxy.business;

import static org.springframework.web.reactive.function.server.ServerResponse.accepted;

import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardSetStorage;
import java.util.stream.Collectors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class FlashcardDeleteCommandHandler implements CommandQueryHandler {

  private FlashcardSetStorage flashcardSetStorage;

  public FlashcardDeleteCommandHandler(FlashcardSetStorage flashcardSetStorage) {
    this.flashcardSetStorage = flashcardSetStorage;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    return Mono.fromSupplier(() -> Void.TYPE)
        .doOnNext((v) -> removeFlashcardFromDefaultSet(serverRequest.pathVariable("id")))
        .flatMap(p -> accepted().build());
  }

  private void removeFlashcardFromDefaultSet(String id) {
    flashcardSetStorage.get("user1@example.com", "Default").subscribe(flashcardSet ->
        flashcardSetStorage.save(flashcardSet.toBuilder()
            .flashcards(flashcardSet.getFlashcards().stream()
                .filter(flashcard -> !flashcard.getId().equals(id))
                .collect(Collectors.toList()))
            .build()));
  }
}
