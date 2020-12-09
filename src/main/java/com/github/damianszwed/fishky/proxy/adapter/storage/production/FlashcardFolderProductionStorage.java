package com.github.damianszwed.fishky.proxy.adapter.storage.production;

import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardFolder;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardFolderService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class FlashcardFolderProductionStorage implements FlashcardFolderService {

  private FlashcardFolderMongoRepository flashcardFolderMongoRepository;

  public FlashcardFolderProductionStorage(
      FlashcardFolderMongoRepository flashcardFolderMongoRepository) {
    this.flashcardFolderMongoRepository = flashcardFolderMongoRepository;
  }

  @Override
  public Flux<FlashcardFolder> get(String owner) {
    return flashcardFolderMongoRepository.findAllByOwner(owner);
  }

  @Override
  public Mono<FlashcardFolder> get(String owner, String flashcardFolderName) {
    return flashcardFolderMongoRepository.findByName(flashcardFolderName);
  }

  @Override
  public Mono<FlashcardFolder> getById(String owner, String flashcardFolderId) {
    return flashcardFolderMongoRepository.findById(flashcardFolderId);
  }

  @Override
  public void save(String owner, FlashcardFolder flashcardFolder) {
    flashcardFolderMongoRepository.save(flashcardFolder)
        .doOnError(throwable -> log.info(throwable.getMessage(), throwable))
        .subscribe(newFlashcardFikder -> log
            .info("FlashcardFolder {} has been saved.", newFlashcardFikder.getId()));
  }

  @Override
  public void remove(String owner, String flashcardFolderId) {
    flashcardFolderMongoRepository.deleteById(flashcardFolderId)
        .doOnError(throwable -> log.info(throwable.getMessage(), throwable))
        .subscribe(o_O -> log.info("FlashcardFolder {} has been removed.", flashcardFolderId));
  }
}
