package com.github.damianszwed.fishky.proxy.adapter.storage.development;

import static java.util.Arrays.asList;

import com.github.damianszwed.fishky.proxy.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardSet;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardSetStorage;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class FlashcardSetDevelopmentStorage implements FlashcardSetStorage {

  private List<FlashcardSet> flashcardSets = new ArrayList<>();

  public FlashcardSetDevelopmentStorage() {
    flashcardSets.add(FlashcardSet.builder()
        .id("dXNlcjFAZXhhbXBsZS5jb20tZGVmYXVsdA==")//user1@example.com-default
        .owner("user1@example.com")
        .name("Default")
        .flashcards(asList(
            Flashcard.builder()
                .id("dXNlcjFAZXhhbXBsZS5jb20tcXVlc3Rpb25h")//user1@example.com-questiona
                .question("questionA")
                .answer("answerA").build(),
            Flashcard.builder()
                .id("dXNlcjFAZXhhbXBsZS5jb20tcXVlc3Rpb25i")//user1@example.com-questionb
                .question("questionB")
                .answer("answerB").build(),
            Flashcard.builder()
                .id("dXNlcjFAZXhhbXBsZS5jb20tcXVlc3Rpb25j")//user1@example.com-questionc
                .question("questionC")
                .answer("answerC").build()
        ))
        .build());
  }

  @Override
  public Flux<FlashcardSet> get(String owner) {
    return Flux.fromIterable(flashcardSets);
  }

  @Override
  public Mono<FlashcardSet> get(String owner, String name) {
    return Mono.justOrEmpty(flashcardSets.stream()
        .filter(flashcardSet -> flashcardSet.getName().equals(name))
        .findFirst());
  }

  @Override
  public Mono<FlashcardSet> getById(String id) {
    return Mono
        .justOrEmpty(
            flashcardSets.stream().filter(flashcardSet -> flashcardSet.getId().equals(id))
                .findFirst());
  }

  @Override
  public void remove(String id) {
    flashcardSets.removeIf(givenFlashcardSet -> givenFlashcardSet.getId().equals(id));
  }

  @Override
  public void save(FlashcardSet flashcardSet) {
    remove(flashcardSet.getId());
    flashcardSets.add(flashcardSet);
  }
}