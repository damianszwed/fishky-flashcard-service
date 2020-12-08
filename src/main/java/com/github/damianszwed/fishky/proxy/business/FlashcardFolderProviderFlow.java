package com.github.damianszwed.fishky.proxy.business;

import com.github.damianszwed.fishky.proxy.port.flashcard.EventSource;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardFolder;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardFolderStorage;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

@Slf4j
public class FlashcardFolderProviderFlow implements EventSource<FlashcardFolder> {

  private final FlashcardFolderStorage flashcardFolderStorage;
  private final Map<String, EmitterProcessor<FlashcardFolder>> emittersByOwners = new HashMap<>();
  private final Map<String, FluxSink<FlashcardFolder>> sinksByOwners = new HashMap<>();

  public FlashcardFolderProviderFlow(FlashcardFolderStorage flashcardFolderStorage) {
    this.flashcardFolderStorage = flashcardFolderStorage;
  }

  void getAll(String owner) {
    final FluxSink<FlashcardFolder> sink = getSink(owner);
    flashcardFolderStorage.get(owner).subscribe(sink::next);
  }

  private FluxSink<FlashcardFolder> getSink(String owner) {
    final EmitterProcessor<FlashcardFolder> processor = getProcessor(owner);
    return sinksByOwners.computeIfAbsent(owner, o_O -> processor.sink());
  }

  @Override
  public Flux<FlashcardFolder> getFlux(String owner) {
    return getProcessor(owner);
  }

  private EmitterProcessor<FlashcardFolder> getProcessor(String owner) {
    return emittersByOwners
        .computeIfAbsent(owner, FlashcardFolderProviderFlow::createEmitterProcessor);
  }

  private static EmitterProcessor<FlashcardFolder> createEmitterProcessor(String o_O) {
    return EmitterProcessor.create();
  }

}
