package com.github.damianszwed.fishky.proxy.port.flashcard;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FlashcardGroupStorage {

  Flux<FlashcardGroup> get(String username);

  Mono<FlashcardGroup> get(String username, String name);

  Mono<FlashcardGroup> getById(String id);

  void save(FlashcardGroup flashcardGroup);

  void remove(String id);
}