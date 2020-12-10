package com.github.damianszwed.fishky.proxy.business;

import com.github.damianszwed.fishky.proxy.port.EventTrigger;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardFolder;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardFolderService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FlashcardFolderEmittingStorage implements FlashcardFolderService {

  private final FlashcardFolderService flashcardFolderStorage;
  private final EventTrigger getAllFoldersEventTrigger;

  public FlashcardFolderEmittingStorage(FlashcardFolderService flashcardFolderStorage,
      EventTrigger getAllFoldersEventTrigger) {
    this.flashcardFolderStorage = flashcardFolderStorage;
    this.getAllFoldersEventTrigger = getAllFoldersEventTrigger;
  }

  @Override
  public Flux<FlashcardFolder> get(String owner) {
    return flashcardFolderStorage.get(owner);
  }

  @Override
  public Mono<FlashcardFolder> get(String owner, String flashcardFolderName) {
    return flashcardFolderStorage.get(owner, flashcardFolderName);
  }

  @Override
  public Mono<FlashcardFolder> getById(String owner, String flashcardFolderId) {
    return flashcardFolderStorage.getById(owner, flashcardFolderId);
  }

  @Override
  public void save(String owner, FlashcardFolder flashcardFolder) {
    flashcardFolderStorage.save(owner, flashcardFolder);
    getAllFoldersEventTrigger.fireUp(owner);//TODO(Damian.Szwed) this for sure wont work yet
  }

  @Override
  public void remove(String owner, String flashcardFolderId) {
    flashcardFolderStorage.remove(owner, flashcardFolderId);
    getAllFoldersEventTrigger.fireUp(owner);//TODO(Damian.Szwed) this for sure wont work yet
  }
}
