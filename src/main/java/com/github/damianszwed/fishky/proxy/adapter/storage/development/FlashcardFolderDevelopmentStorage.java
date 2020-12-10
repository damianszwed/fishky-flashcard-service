package com.github.damianszwed.fishky.proxy.adapter.storage.development;

import static java.util.Arrays.asList;

import com.github.damianszwed.fishky.proxy.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardFolder;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardFolderService;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class FlashcardFolderDevelopmentStorage implements FlashcardFolderService {

  private List<FlashcardFolder> flashcardFolders = new ArrayList<>();

  public FlashcardFolderDevelopmentStorage() {
    flashcardFolders.add(FlashcardFolder.builder()
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
  public Flux<FlashcardFolder> get(String owner) {
    return Flux.fromIterable(flashcardFolders);
  }

  @Override
  public Mono<FlashcardFolder> get(String owner, String flashcardFolderName) {
    return Mono.justOrEmpty(flashcardFolders.stream()
        .filter(flashcardFolder -> flashcardFolder.getName().equals(flashcardFolderName))
        .findFirst());
  }

  @Override
  public Mono<FlashcardFolder> getById(String owner, String flashcardFolderId) {
    return Mono
        .justOrEmpty(
            flashcardFolders.stream().filter(flashcardFolder -> flashcardFolder.getId().equals(
                flashcardFolderId))
                .findFirst());
  }

  @Override
  public Mono<Void> remove(String owner, String flashcardFolderId) {
    return Mono.fromSupplier(() -> Void.TYPE)
        .doOnNext((v) ->
            flashcardFolders.removeIf(givenFlashcardFolder ->
                givenFlashcardFolder.getId().equals(flashcardFolderId)))
        .then();
  }

  @Override
  public Mono<FlashcardFolder> save(String owner, FlashcardFolder flashcardFolder) {
    return remove(owner, flashcardFolder.getId())
        .doOnTerminate(() -> flashcardFolders.add(flashcardFolder))
        .then(Mono.just(flashcardFolder));
  }
}