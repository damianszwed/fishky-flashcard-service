package com.github.damianszwed.fishky.proxy.adapter.storage.development;

import static java.util.Arrays.asList;

import com.github.damianszwed.fishky.proxy.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardGroup;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardGroupStorage;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class FlashcardGroupDevelopmentStorage implements FlashcardGroupStorage {

  private List<FlashcardGroup> flashcardGroups = new ArrayList<>();

  public FlashcardGroupDevelopmentStorage() {
    flashcardGroups.add(FlashcardGroup.builder()
        .id("dXNlcjFAZXhhbXBsZS5jb20tZGVmYXVsdA==")//user1@example.com-default
        .owner("user1@example.com")
        .name("default")
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
  public Flux<FlashcardGroup> get(String owner) {
    return Flux.fromIterable(flashcardGroups);
  }

  @Override
  public Mono<FlashcardGroup> get(String owner, String name) {
    return Mono.justOrEmpty(flashcardGroups.stream()
        .filter(flashcardGroup -> flashcardGroup.getName().equals(name))
        .findFirst());
  }

  @Override
  public Mono<FlashcardGroup> getById(String id) {
    return Mono
        .justOrEmpty(
            flashcardGroups.stream().filter(flashcardGroup -> flashcardGroup.getId().equals(id))
                .findFirst());
  }

  @Override
  public void remove(String id) {
    flashcardGroups.removeIf(givenFlashcardGroup -> givenFlashcardGroup.getId().equals(id));
  }

  @Override
  public void save(FlashcardGroup flashcardGroup) {
    remove(flashcardGroup.getId());
    flashcardGroups.add(flashcardGroup);
  }
}