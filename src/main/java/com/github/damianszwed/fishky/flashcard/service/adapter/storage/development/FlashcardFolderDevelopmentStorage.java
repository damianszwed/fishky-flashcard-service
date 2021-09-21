package com.github.damianszwed.fishky.flashcard.service.adapter.storage.development;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import com.github.damianszwed.fishky.flashcard.service.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolder;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolderService;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class FlashcardFolderDevelopmentStorage implements FlashcardFolderService {

  private final List<FlashcardFolder> flashcardFolders = new ArrayList<>();
  @SuppressWarnings("FieldCanBeLocal")
  private final FlashcardFolder user1Folder = FlashcardFolder.builder()
      .id("dXNlcjFAZXhhbXBsZS5jb20tZGVmYXVsdA==")//user1@example.com-default
      .owner("user1@example.com")
      .name("Default")
      .flashcards(asList(
          Flashcard.builder()
              .id("dXNlcjFAZXhhbXBsZS5jb20tcXVlc3Rpb25h")//user1@example.com-questiona
              .question("questionA")
              .answers(singletonList("answerA")).build(),
          Flashcard.builder()
              .id("dXNlcjFAZXhhbXBsZS5jb20tcXVlc3Rpb25i")//user1@example.com-questionb
              .question("questionB")
              .answers(singletonList("answerB")).build(),
          Flashcard.builder()
              .id("dXNlcjFAZXhhbXBsZS5jb20tcXVlc3Rpb25j")//user1@example.com-questionc
              .question("questionC")
              .answers(singletonList("answerC")).build()
      ))
      .build();
  @SuppressWarnings("FieldCanBeLocal")
  private final FlashcardFolder broughtInFolder = FlashcardFolder.builder()
      .id("YnJvdWdodGluLXR1cmlzbQ==")//broughtin-turism
      .owner("broughtin")
      .name("Turism")
      .flashcards(singletonList(
          Flashcard.builder()
              .id("YnJvdWdodGluLWV4Y3Vyc2lvbg==")//broughtin-excursion
              .question("excursion")
              .answers(asList("wycieczka", "wyprawa", "wypad")).build()
      ))
      .build();

  public FlashcardFolderDevelopmentStorage() {
    flashcardFolders.add(user1Folder);
    flashcardFolders.add(broughtInFolder);
  }

  @Override
  public Flux<FlashcardFolder> get(String owner) {
    return Flux.fromIterable(flashcardFolders)
        .filter(flashcardFolder -> owner.equals(flashcardFolder.getOwner()));
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