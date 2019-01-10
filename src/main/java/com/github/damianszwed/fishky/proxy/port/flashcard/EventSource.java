package com.github.damianszwed.fishky.proxy.port.flashcard;

import reactor.core.publisher.Flux;

public interface EventSource {
  Flux<Flashcard> getFlux();
}
