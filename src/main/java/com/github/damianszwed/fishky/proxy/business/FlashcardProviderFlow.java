package com.github.damianszwed.fishky.proxy.business;

import com.github.damianszwed.fishky.proxy.port.flashcard.EventSource;
import com.github.damianszwed.fishky.proxy.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardFolder;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardFolderService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.TopicProcessor;

@Deprecated
public class FlashcardProviderFlow implements EventSource<Flashcard> {

  private final FlashcardFolderService flashcardFolderService;
  private final TopicProcessor<Flashcard> unicastProcessor;

  public FlashcardProviderFlow(FlashcardFolderService flashcardFolderService) {
    this.flashcardFolderService = flashcardFolderService;
    unicastProcessor = TopicProcessor.create();
  }

  void getAll() {
    flashcardFolderService.get("user1@example.com", "Default")
        .flatMapIterable(FlashcardFolder::getFlashcards)
        .subscribe(unicastProcessor::onNext);
  }

  @Override
  public Flux<Flashcard> getFlux(String owner) {
    return unicastProcessor;
  }
}
