package com.github.damianszwed.fishky.proxy.business;

import static org.springframework.web.reactive.function.server.ServerResponse.accepted;

import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import com.github.damianszwed.fishky.proxy.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardGroup;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardGroupStorage;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class FlashcardGroupDeleteFlashcardCommandHandler implements CommandQueryHandler {

  private FlashcardGroupStorage flashcardGroupStorage;

  public FlashcardGroupDeleteFlashcardCommandHandler(FlashcardGroupStorage flashcardGroupStorage) {
    this.flashcardGroupStorage = flashcardGroupStorage;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    String flashcardGroupId = serverRequest.pathVariable("flashcardGroupId");
    String flashcardId = serverRequest.pathVariable("flashcardId");
    return Mono.fromSupplier(() -> Void.TYPE)
        .doOnNext((v) -> removeFlashcardFromFlashcardGroup(flashcardGroupId, flashcardId))
        .flatMap(p -> accepted().build());
  }

  private void removeFlashcardFromFlashcardGroup(
      String flashcardGroupId,
      String flashcardId) {
    flashcardGroupStorage.getById(flashcardGroupId)
        .subscribe(flashcardGroup ->
            flashcardGroupStorage.save(
                flashcardGroup.toBuilder()
                    .flashcards(withRemovedParticularFlashcard(flashcardId, flashcardGroup)
                        .collect(Collectors.toList()))
                    .build()));
  }

  private Stream<Flashcard> withRemovedParticularFlashcard(
      String flashcardId,
      FlashcardGroup flashcardGroup) {
    return flashcardGroup.getFlashcards().stream()
        .filter(flashcard -> !flashcard.getId()
            .equals(flashcardId));
  }
}
