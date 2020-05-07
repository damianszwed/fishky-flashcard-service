package com.github.damianszwed.fishky.proxy.business;

import static org.springframework.web.reactive.function.server.ServerResponse.accepted;

import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import com.github.damianszwed.fishky.proxy.port.IdEncoderDecoder;
import com.github.damianszwed.fishky.proxy.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardSet;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardSetStorage;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class FlashcardSaveCommandHandler implements CommandQueryHandler {

  private final FlashcardSetStorage flashcardSetStorage;
  private final IdEncoderDecoder idEncoderDecoder;

  public FlashcardSaveCommandHandler(
      FlashcardSetStorage flashcardSetStorage,
      IdEncoderDecoder idEncoderDecoder) {
    this.flashcardSetStorage = flashcardSetStorage;
    this.idEncoderDecoder = idEncoderDecoder;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    return serverRequest.bodyToMono(Flashcard.class)
        .doOnNext(this::saveFlashcardToDefaultSet)
        .flatMap(flashcard -> accepted().build());
  }

  private void saveFlashcardToDefaultSet(Flashcard flashcardToSave) {
    flashcardSetStorage.get("user1@example.com", "Default")
        .switchIfEmpty(createFirstDefaultFlashcardSet())
        .subscribe(flashcardSet -> {
          String id = idEncoderDecoder.encodeId("user1@example.com", flashcardToSave.getQuestion());
          List<Flashcard> flashcardsWithOneRemoved = flashcardSet.getFlashcards().stream()
              .filter(flashcard -> !flashcard.getId().equals(id)).collect(Collectors.toList());
          flashcardsWithOneRemoved.add(flashcardToSave
              .toBuilder()
              .id(id)
              .build());
          flashcardSetStorage.save(flashcardSet
              .toBuilder()
              .flashcards(flashcardsWithOneRemoved)
              .build());
        });
  }

  /**
   * Creates first default flashcard set if user doesn't have any set.
   *
   * @return first Default flashcard set
   */
  private Mono<FlashcardSet> createFirstDefaultFlashcardSet() {
    return Mono.just(FlashcardSet.builder()
        .id("dXNlcjFAZXhhbXBsZS5jb20tZGVmYXVsdA==")//user1@example.com-default
        .owner("user1@example.com")
        .name("Default")
        .build());
  }
}
