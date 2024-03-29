package com.github.damianszwed.fishky.flashcard.service.adapter.storage.mongo;

import com.github.damianszwed.fishky.flashcard.service.adapter.storage.entity.FlashcardFolder;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolderStorage;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class FlashcardFolderProductionStorage implements FlashcardFolderStorage {

  private final FlashcardFolderMongoRepository flashcardFolderMongoRepository;

  public FlashcardFolderProductionStorage(
      FlashcardFolderMongoRepository flashcardFolderMongoRepository) {
    this.flashcardFolderMongoRepository = flashcardFolderMongoRepository;
  }

  @Override
  public Flux<FlashcardFolder> get() {
    return flashcardFolderMongoRepository.findAll();
  }

  @Override
  public Flux<FlashcardFolder> get(String owner) {
    return flashcardFolderMongoRepository.findAllByOwner(owner);
  }

  @Override
  public Mono<FlashcardFolder> get(String owner, String flashcardFolderName) {
    return flashcardFolderMongoRepository.findByOwnerAndName(owner, flashcardFolderName);
  }

  @Override
  public Mono<FlashcardFolder> getById(String owner, String flashcardFolderId) {
    return flashcardFolderMongoRepository.findById(flashcardFolderId);
  }

  @Override
  public Mono<FlashcardFolder> save(String owner, FlashcardFolder flashcardFolder) {
    return flashcardFolderMongoRepository.save(flashcardFolder)
        .doOnError(throwable -> log.info(throwable.getMessage(), throwable));
  }

  @Override
  public Mono<Void> remove(String owner, String flashcardFolderId) {
    return flashcardFolderMongoRepository.deleteById(flashcardFolderId)
        .doOnError(throwable -> log.info(throwable.getMessage(), throwable));
  }
}
