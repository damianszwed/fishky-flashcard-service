package com.github.damianszwed.fishky.proxy.adapter.storage;

import com.github.damianszwed.fishky.proxy.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardGroup;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardGroupStorage;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardStorage;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class FlashcardStorageAdapter implements FlashcardStorage {

  private FlashcardGroupStorage flashcardGroupStorage;

  public FlashcardStorageAdapter(FlashcardGroupStorage flashcardGroupStorage) {
    this.flashcardGroupStorage = flashcardGroupStorage;
  }

  @Override
  public Flux<Flashcard> get(String username) {
    return flashcardGroupStorage.get("any", "default")
        .flatMapIterable(FlashcardGroup::getFlashcards);
  }

  @Override
  public void remove(String id) {
    flashcardGroupStorage.get("any", "default").subscribe(flashcardGroup ->
        flashcardGroupStorage.save(flashcardGroup.toBuilder()
            .flashcards(flashcardGroup.getFlashcards().stream()
                .filter(flashcard -> !flashcard.getId().equals(id)).collect(Collectors.toList()))
            .build()));
  }

  @Override
  public void save(Flashcard flashcardToSave) {
    flashcardGroupStorage.get("any", "default")
        .switchIfEmpty(createFirstDefaultFlashcardGroup())
        .subscribe(flashcardGroup -> {
          //TODO(Damian.Szwed) flashcard id generation
          //TODO(Damian.Szwed) move it to handler
          String id = "user1@example.com-" + flashcardToSave.getQuestion().toLowerCase();
          //TODO(Damian.Szwed) stary, to nie powinien być HashMap jednak?
          List<Flashcard> flashcardsWithOneRemoved = flashcardGroup.getFlashcards().stream()
              .filter(flashcard -> !flashcard.getId().equals(id)).collect(Collectors.toList());
          flashcardsWithOneRemoved.add(flashcardToSave
              .toBuilder()
              .id(id)
              .build());
          flashcardGroupStorage.save(flashcardGroup
              .toBuilder()
              .flashcards(flashcardsWithOneRemoved)
              .build());
        });
  }

  /**
   * Creates first default flashcard group if user doesn't have any groups.
   *
   * @return first Default flashcard group
   */
  private Mono<FlashcardGroup> createFirstDefaultFlashcardGroup() {
    return Mono.just(FlashcardGroup.builder()
        .id("user1@example.com-default")
        .owner("user1@example.com")
        .name("default")
        .build());
  }
}
