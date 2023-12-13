package com.github.damianszwed.fishky.flashcard.service.business;

import com.github.damianszwed.fishky.flashcard.service.adapter.storage.entity.FlashcardFolder;
import com.github.damianszwed.fishky.flashcard.service.port.EventTrigger;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.EventSource;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolderService;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.Many;
import reactor.util.concurrent.Queues;

@Slf4j
public class FlashcardFolderProviderFlow implements EventSource<FlashcardFolder>, EventTrigger {

  private final FlashcardFolderService flashcardFolderService;
  private final Map<String, Sinks.Many<FlashcardFolder>> emittersByOwners = new HashMap<>();

  public FlashcardFolderProviderFlow(FlashcardFolderService flashcardFolderService) {
    this.flashcardFolderService = flashcardFolderService;
  }

  @Override
  public void fireUp(String owner) {
    final Many<FlashcardFolder> flashcardFolderMany = getFlashcardFolderMany(owner);
    flashcardFolderService.get(owner).subscribe(flashcardFolderMany::tryEmitNext);
  }

  @Override
  public Flux<FlashcardFolder> getFlux(String owner) {
    log.info("Owner {} gets SSE.", owner);
    return getFlashcardFolderMany(owner).asFlux();
  }

  private Many<FlashcardFolder> getFlashcardFolderMany(String owner) {
    return emittersByOwners
        .computeIfAbsent(owner, s -> {
          log.info("Building Sinks.Many for owner {}.", owner);
          return Sinks.many()
              .multicast().onBackpressureBuffer(
                  Queues.SMALL_BUFFER_SIZE, false);
        });
  }

}
