package com.github.damianszwed.fishky.proxy.business;

import com.github.damianszwed.fishky.proxy.port.flashcard.EventSource;
import com.github.damianszwed.fishky.proxy.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardFolder;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardFolderStorage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.TopicProcessor;

public class FlashcardProviderFlow implements EventSource<Flashcard> {

  private FlashcardFolderStorage flashcardFolderStorage;
  private final TopicProcessor<Flashcard> unicastProcessor;

  public FlashcardProviderFlow(FlashcardFolderStorage flashcardFolderStorage) {
    this.flashcardFolderStorage = flashcardFolderStorage;
    unicastProcessor = TopicProcessor.create();
  }

  void getAll() {
    flashcardFolderStorage.get("user1@example.com", "Default")
        .flatMapIterable(FlashcardFolder::getFlashcards)
        .subscribe(unicastProcessor::onNext);
  }

  @Override
  public Flux<Flashcard> getFlux(String owner) {
    return unicastProcessor.publish().autoConnect();
  }
}
