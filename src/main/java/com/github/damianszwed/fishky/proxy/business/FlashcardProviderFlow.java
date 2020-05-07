package com.github.damianszwed.fishky.proxy.business;

import com.github.damianszwed.fishky.proxy.port.flashcard.EventSource;
import com.github.damianszwed.fishky.proxy.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardSet;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardSetStorage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.TopicProcessor;

public class FlashcardProviderFlow implements EventSource {

  private FlashcardSetStorage flashcardSetStorage;
  private final TopicProcessor<Flashcard> unicastProcessor;

  public FlashcardProviderFlow(FlashcardSetStorage flashcardSetStorage) {
    this.flashcardSetStorage = flashcardSetStorage;
    unicastProcessor = TopicProcessor.create();
  }

  void getAll() {
    flashcardSetStorage.get("user1@example.com", "Default")
        .flatMapIterable(FlashcardSet::getFlashcards)
        .subscribe(unicastProcessor::onNext);
  }

  @Override
  public Flux<Flashcard> getFlux() {
    return unicastProcessor.publish().autoConnect();
  }
}
