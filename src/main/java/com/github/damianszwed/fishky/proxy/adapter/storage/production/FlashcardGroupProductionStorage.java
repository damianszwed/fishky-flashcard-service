package com.github.damianszwed.fishky.proxy.adapter.storage.production;

import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardGroup;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardGroupStorage;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class FlashcardGroupProductionStorage implements FlashcardGroupStorage {

  private FlashcardGroupMongoRepository flashcardMongoRepository;

  public FlashcardGroupProductionStorage(FlashcardGroupMongoRepository flashcardMongoRepository) {
    this.flashcardMongoRepository = flashcardMongoRepository;
  }

  @Override
  public Flux<FlashcardGroup> get(String username) {
    return flashcardMongoRepository.findAll();
  }

  @Override
  public Mono<FlashcardGroup> get(String username, String name) {
    return flashcardMongoRepository.findByName(name);
  }

  @Override
  public void save(FlashcardGroup flashcardGroup) {
    flashcardMongoRepository.save(flashcardGroup)
        .doOnError(throwable -> log.info(throwable.getMessage(), throwable))
        .subscribe(newFlashcardGroup -> log
            .info("FlashcardGroup {} has been saved.", newFlashcardGroup.getId()));
  }

  @Override
  public void remove(String id) {
    flashcardMongoRepository.deleteById(id)
        .doOnError(throwable -> log.info(throwable.getMessage(), throwable))
        .subscribe(o_O -> log.info("FlashcardGroup {} has been removed.", id));
  }
}
