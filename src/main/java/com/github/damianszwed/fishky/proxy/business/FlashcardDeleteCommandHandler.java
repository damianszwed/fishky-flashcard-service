package com.github.damianszwed.fishky.proxy.business;

import static org.springframework.web.reactive.function.server.ServerResponse.accepted;

import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardGroupStorage;
import java.util.stream.Collectors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class FlashcardDeleteCommandHandler implements CommandQueryHandler {

  private FlashcardGroupStorage flashcardGroupStorage;

  public FlashcardDeleteCommandHandler(FlashcardGroupStorage flashcardGroupStorage) {
    this.flashcardGroupStorage = flashcardGroupStorage;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    return Mono.fromSupplier(() -> Void.TYPE)
        .doOnNext((v) -> removeFlashcardFromDefaultGroup(serverRequest.pathVariable("id")))
        .flatMap(p -> accepted().build());
  }

  private void removeFlashcardFromDefaultGroup(String id) {
    flashcardGroupStorage.get("user1@example.com", "default").subscribe(flashcardGroup ->
        flashcardGroupStorage.save(flashcardGroup.toBuilder()
            .flashcards(flashcardGroup.getFlashcards().stream()
                .filter(flashcard -> !flashcard.getId().equals(id))
                .collect(Collectors.toList()))
            .build()));
  }
}
