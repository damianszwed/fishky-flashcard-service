package com.github.damianszwed.fishky.proxy.adapter.storage.production;

import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardFolder;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardFolderStorage;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class FlashcardFolderProductionStorage implements FlashcardFolderStorage {

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
  public Mono<FlashcardFolder> get(String owner, String name) {
    return flashcardFolderMongoRepository.findByName(name);
  }

  @Override
  public Mono<FlashcardFolder> getById(String id) {
    return flashcardFolderMongoRepository.findById(id);
  }

  @Override
  public void save(FlashcardFolder flashcardFolder) {
    flashcardFolderMongoRepository.save(flashcardFolder)
        .doOnError(throwable -> log.info(throwable.getMessage(), throwable))
        .subscribe(newFlashcardFikder -> log
            .info("FlashcardFolder {} has been saved.", newFlashcardFikder.getId()));
  }

  @Override
  public void remove(String id) {
    flashcardFolderMongoRepository.deleteById(id)
        .doOnError(throwable -> log.info(throwable.getMessage(), throwable))
        .subscribe(o_O -> log.info("FlashcardFolder {} has been removed.", id));
  }
}
