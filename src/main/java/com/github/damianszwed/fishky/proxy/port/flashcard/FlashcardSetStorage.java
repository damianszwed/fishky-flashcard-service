package com.github.damianszwed.fishky.proxy.port.flashcard;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FlashcardSetStorage {

  Flux<FlashcardSet> get(String owner);

  Mono<FlashcardSet> get(String owner,
      String name);//TODO(Damian.Szwed) rename name to flashcardSetName

  Mono<FlashcardSet> getById(String id);//TODO(Damian.Szwed) rename to flashcardSetId

  void save(FlashcardSet flashcardSet);

  void remove(String id);//TODO(Damian.Szwed) rename to flashcardSetId
}