package com.github.damianszwed.fishky.flashcard.service.business;

import com.github.damianszwed.fishky.flashcard.service.port.EventTrigger;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolder;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolderService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class FlashcardFolderEmittingStorage implements FlashcardFolderService {

  private final FlashcardFolderService flashcardFolderStorage;
  private final EventTrigger getAllFoldersEventTrigger;

  public FlashcardFolderEmittingStorage(FlashcardFolderService flashcardFolderStorage,
      EventTrigger getAllFoldersEventTrigger) {
    this.flashcardFolderStorage = flashcardFolderStorage;
    this.getAllFoldersEventTrigger = getAllFoldersEventTrigger;
  }

  @Override
  public Flux<FlashcardFolder> get() {
    log.info("Getting all folders for search reindexing purposes..");
    return flashcardFolderStorage.get();
  }

  @Override
  public Flux<FlashcardFolder> get(String owner) {
    log.info("User {} gets folders.", owner);
    return flashcardFolderStorage.get(owner);
  }

  @Override
  public Mono<FlashcardFolder> get(String owner, String flashcardFolderName) {
    log.info("User {} gets flashcardFolder {}.", owner, flashcardFolderName);
    return flashcardFolderStorage.get(owner, flashcardFolderName);
  }

  @Override
  public Mono<FlashcardFolder> getById(String owner, String flashcardFolderId) {
    log.info("User {} gets flashcardFolder {}.", owner, flashcardFolderId);
    return flashcardFolderStorage.getById(owner, flashcardFolderId);
  }

  @Override
  public Mono<FlashcardFolder> save(String owner, FlashcardFolder flashcardFolder) {
    log.info("Owner {} saves flashcardFolder {}.", owner, flashcardFolder);
    return flashcardFolderStorage.save(owner, flashcardFolder)
        .doOnNext(folder -> {
          log.info("save folder - doOnNext -> getAllFoldersEventTrigger.fireUp(owner)");
          getAllFoldersEventTrigger.fireUp(owner);
        });
  }

  @Override
  public Mono<Void> remove(String owner, String flashcardFolderId) {
    log.info("Owner {} removes flashcardFolder {}.", owner, flashcardFolderId);
    return flashcardFolderStorage.remove(owner, flashcardFolderId)
        .doOnTerminate(() -> {
          log.info("remove folder - doOnTerminate -> getAllFoldersEventTrigger.fireUp(owner)");
          getAllFoldersEventTrigger.fireUp(owner);
        });
  }
}
