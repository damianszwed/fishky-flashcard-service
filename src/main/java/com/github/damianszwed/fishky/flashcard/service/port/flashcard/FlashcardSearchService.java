package com.github.damianszwed.fishky.flashcard.service.port.flashcard;

import com.github.damianszwed.fishky.flashcard.service.adapter.storage.entity.FlashcardFolder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FlashcardSearchService {

  Flux<FlashcardFolder> search(String owner, String text);

  Mono<Void> reindex();
}
