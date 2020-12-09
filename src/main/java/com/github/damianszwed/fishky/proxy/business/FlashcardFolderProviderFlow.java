package com.github.damianszwed.fishky.proxy.business;

import com.github.damianszwed.fishky.proxy.port.EventTrigger;
import com.github.damianszwed.fishky.proxy.port.flashcard.EventSource;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardFolder;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardFolderService;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

@Slf4j
public class FlashcardFolderProviderFlow implements EventSource<FlashcardFolder>, EventTrigger {

  private final FlashcardFolderService flashcardFolderService;
  private final Map<String, EmitterProcessor<FlashcardFolder>> emittersByOwners = new HashMap<>();
  private final Map<String, FluxSink<FlashcardFolder>> sinksByOwners = new HashMap<>();

  public FlashcardFolderProviderFlow(FlashcardFolderService flashcardFolderService) {
    this.flashcardFolderService = flashcardFolderService;
  }

  @Override
  public void fireUp(String owner) {
    final FluxSink<FlashcardFolder> sink = getSink(owner);
    flashcardFolderService.get(owner).subscribe(sink::next);
  }

  @Override
  public Flux<FlashcardFolder> getFlux(String owner) {
    return getProcessor(owner);
  }

  private FluxSink<FlashcardFolder> getSink(String owner) {
    final EmitterProcessor<FlashcardFolder> processor = getProcessor(owner);
    return sinksByOwners.computeIfAbsent(owner, o_O -> processor.sink());
  }

  private EmitterProcessor<FlashcardFolder> getProcessor(String owner) {
    return emittersByOwners
        .computeIfAbsent(owner, FlashcardFolderProviderFlow::createEmitterProcessor);
  }

  private static EmitterProcessor<FlashcardFolder> createEmitterProcessor(String o_O) {
    return EmitterProcessor.create();
  }

}