package com.github.damianszwed.fishky.proxy.business;

import static org.springframework.web.reactive.function.server.ServerResponse.accepted;

import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import com.github.damianszwed.fishky.proxy.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardSet;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardSetStorage;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class FlashcardSetDeleteFlashcardCommandHandler implements CommandQueryHandler {

  private FlashcardSetStorage flashcardSetStorage;

  public FlashcardSetDeleteFlashcardCommandHandler(FlashcardSetStorage flashcardSetStorage) {
    this.flashcardSetStorage = flashcardSetStorage;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    String flashcardSetId = serverRequest.pathVariable("flashcardSetId");
    String flashcardId = serverRequest.pathVariable("flashcardId");
    return Mono.fromSupplier(() -> Void.TYPE)
        .doOnNext((v) -> removeFlashcardFromFlashcardSet(flashcardSetId, flashcardId))
        .flatMap(p -> accepted().build());
  }

  private void removeFlashcardFromFlashcardSet(
      String flashcardSetId,
      String flashcardId) {
    flashcardSetStorage.getById(flashcardSetId)
        .subscribe(flashcardSet ->
            flashcardSetStorage.save(
                flashcardSet.toBuilder()
                    .flashcards(withRemovedParticularFlashcard(flashcardId, flashcardSet)
                        .collect(Collectors.toList()))
                    .build()));
  }

  private Stream<Flashcard> withRemovedParticularFlashcard(
      String flashcardId,
      FlashcardSet flashcardSet) {
    return flashcardSet.getFlashcards().stream()
        .filter(flashcard -> !flashcard.getId()
            .equals(flashcardId));
  }
}
