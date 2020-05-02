package com.github.damianszwed.fishky.proxy.adapter.storage.production;

import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardGroup;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardGroupStorage;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class FlashcardGroupProductionStorage implements FlashcardGroupStorage {

  private FlashcardGroupMongoRepository flashcardGroupMongoRepository;

  public FlashcardGroupProductionStorage(
      FlashcardGroupMongoRepository flashcardGroupMongoRepository) {
    this.flashcardGroupMongoRepository = flashcardGroupMongoRepository;
  }

  @Override
  public Flux<FlashcardGroup> get(String owner) {
    return flashcardGroupMongoRepository.findAll();
  }

  @Override
  public Mono<FlashcardGroup> get(String owner, String name) {
    return flashcardGroupMongoRepository.findByName(name);
  }

  @Override
  public Mono<FlashcardGroup> getById(String id) {
    return flashcardGroupMongoRepository.findById(id);
  }

  @Override
  public void save(FlashcardGroup flashcardGroup) {
    flashcardGroupMongoRepository.save(flashcardGroup)
        .doOnError(throwable -> log.info(throwable.getMessage(), throwable))
        .subscribe(newFlashcardGroup -> log
            .info("FlashcardGroup {} has been saved.", newFlashcardGroup.getId()));
  }

  @Override
  public void remove(String id) {
    flashcardGroupMongoRepository.deleteById(id)
        .doOnError(throwable -> log.info(throwable.getMessage(), throwable))
        .subscribe(o_O -> log.info("FlashcardGroup {} has been removed.", id));
  }
}
