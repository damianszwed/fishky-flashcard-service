package com.github.damianszwed.fishky.proxy.adapter.storage.production;

import com.github.damianszwed.fishky.proxy.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardProvider;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardRemover;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardSaver;
import reactor.core.publisher.Flux;

public class FlashcardProductionStorage implements FlashcardProvider, FlashcardRemover,
    FlashcardSaver {

  private FlashcardMongoRepository flashcardMongoRepository;

  public FlashcardProductionStorage(FlashcardMongoRepository flashcardMongoRepository) {
    this.flashcardMongoRepository = flashcardMongoRepository;
  }

  @Override
  public Flux<Flashcard> get(String username) {
    return flashcardMongoRepository.findAll();
  }

  @Override
  public void remove(String id) {
    flashcardMongoRepository.deleteById(id);
  }

  @Override
  public void save(Flashcard flashcard) {
    flashcardMongoRepository.save(flashcard);
  }
}
