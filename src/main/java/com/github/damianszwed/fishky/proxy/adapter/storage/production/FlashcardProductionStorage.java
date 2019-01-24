package com.github.damianszwed.fishky.proxy.adapter.storage.production;

import com.github.damianszwed.fishky.proxy.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardProvider;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardRemover;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardSaver;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
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
    flashcardMongoRepository.deleteById(id).subscribe();
  }

  @Override
  public void save(Flashcard flashcard) {
    flashcardMongoRepository.save(
        flashcard
            .toBuilder()
            //TODO(damian.szwed) id generation
            .id("user1@example.com-" + flashcard.getQuestion().toLowerCase())
            .build())
        .doOnError(throwable -> log.info(throwable.getMessage(), throwable))
        .subscribe(newFlashcard -> log.info(newFlashcard.getId() + " has been created"));
  }
}
