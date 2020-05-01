package com.github.damianszwed.fishky.proxy.business;

import com.github.damianszwed.fishky.proxy.port.flashcard.EventSource;
import com.github.damianszwed.fishky.proxy.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardStorage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.TopicProcessor;

public class FlashcardProviderFlow implements EventSource {

  private FlashcardStorage flashcardStorage;
  private final TopicProcessor<Flashcard> unicastProcessor;

  public FlashcardProviderFlow(FlashcardStorage flashcardStorage) {
    this.flashcardStorage = flashcardStorage;
    unicastProcessor = TopicProcessor.create();
  }

  void getAll() {
    flashcardStorage.get("any")
        .subscribe(unicastProcessor::onNext);
  }

  @Override
  public Flux<Flashcard> getFlux() {
    return unicastProcessor.publish().autoConnect();
  }
}
