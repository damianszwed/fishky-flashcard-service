package com.github.damianszwed.fishky.proxy.port.flashcard;

import reactor.core.publisher.Flux;

public interface FlashcardStorage {

  Flux<Flashcard> get(String username);

  void save(Flashcard flashcard);

  void remove(String id);
}