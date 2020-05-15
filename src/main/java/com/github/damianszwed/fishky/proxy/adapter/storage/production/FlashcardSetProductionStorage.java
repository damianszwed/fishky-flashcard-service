package com.github.damianszwed.fishky.proxy.adapter.storage.production;

import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardSet;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardSetStorage;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class FlashcardSetProductionStorage implements FlashcardSetStorage {

  private FlashcardSetMongoRepository flashcardSetMongoRepository;

  public FlashcardSetProductionStorage(
      FlashcardSetMongoRepository flashcardSetMongoRepository) {
    this.flashcardSetMongoRepository = flashcardSetMongoRepository;
  }

  @Override
  public Flux<FlashcardSet> get(String owner) {
    return flashcardSetMongoRepository.findAllByOwner(owner);
  }

  @Override
  public Mono<FlashcardSet> get(String owner, String name) {
    return flashcardSetMongoRepository.findByName(name);
  }

  @Override
  public Mono<FlashcardSet> getById(String id) {
    return flashcardSetMongoRepository.findById(id);
  }

  @Override
  public void save(FlashcardSet flashcardSet) {
    flashcardSetMongoRepository.save(flashcardSet)
        .doOnError(throwable -> log.info(throwable.getMessage(), throwable))
        .subscribe(newFlashcardSet -> log
            .info("FlashcardSet {} has been saved.", newFlashcardSet.getId()));
  }

  @Override
  public void remove(String id) {
    flashcardSetMongoRepository.deleteById(id)
        .doOnError(throwable -> log.info(throwable.getMessage(), throwable))
        .subscribe(o_O -> log.info("FlashcardSet {} has been removed.", id));
  }
}
