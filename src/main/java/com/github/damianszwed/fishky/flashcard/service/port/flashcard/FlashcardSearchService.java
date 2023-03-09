package com.github.damianszwed.fishky.flashcard.service.port.flashcard;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FlashcardSearchService {

  Flux<FlashcardFolder> search(String owner, String text);

  Mono<Void> reindex();
}
