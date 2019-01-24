package com.github.damianszwed.fishky.proxy.port.flashcard;

import reactor.core.publisher.Flux;

public interface FlashcardProvider {

  Flux<Flashcard> get(String username);
}