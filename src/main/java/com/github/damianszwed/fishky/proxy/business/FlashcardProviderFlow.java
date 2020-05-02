package com.github.damianszwed.fishky.proxy.business;

import com.github.damianszwed.fishky.proxy.port.flashcard.EventSource;
import com.github.damianszwed.fishky.proxy.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardGroup;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardGroupStorage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.TopicProcessor;

public class FlashcardProviderFlow implements EventSource {

  private FlashcardGroupStorage flashcardGroupStorage;
  private final TopicProcessor<Flashcard> unicastProcessor;

  public FlashcardProviderFlow(FlashcardGroupStorage flashcardGroupStorage) {
    this.flashcardGroupStorage = flashcardGroupStorage;
    unicastProcessor = TopicProcessor.create();
  }

  void getAll() {
    flashcardGroupStorage.get("any", "default")
        .flatMapIterable(FlashcardGroup::getFlashcards)
        .subscribe(unicastProcessor::onNext);
  }

  @Override
  public Flux<Flashcard> getFlux() {
    return unicastProcessor.publish().autoConnect();
  }
}
