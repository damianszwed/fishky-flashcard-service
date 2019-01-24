package com.github.damianszwed.fishky.proxy.business;

import com.github.damianszwed.fishky.proxy.port.flashcard.EventSource;
import com.github.damianszwed.fishky.proxy.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardProvider;
import reactor.core.publisher.Flux;
import reactor.core.publisher.TopicProcessor;

public class FlashcardProviderFlow implements EventSource {

  private FlashcardProvider flashcardProvider;
  private final TopicProcessor<Flashcard> unicastProcessor;

  public FlashcardProviderFlow(FlashcardProvider flashcardProvider) {

    this.flashcardProvider = flashcardProvider;
    unicastProcessor = TopicProcessor.create();
  }

  void getAll() {
    flashcardProvider.get("any")
        .subscribe(unicastProcessor::onNext);
  }

  @Override
  public Flux<Flashcard> getFlux() {
    return unicastProcessor.publish().autoConnect();
  }
}
