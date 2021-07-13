package com.github.damianszwed.fishky.flashcard.service.port.flashcard;

import reactor.core.publisher.Flux;

public interface EventSource<T> {

  Flux<T> getFlux(String owner);
}
