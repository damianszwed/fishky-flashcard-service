package com.github.damianszwed.fishky.flashcard.service.port.flashcard;

import reactor.core.publisher.Flux;

public interface FlashcardSearchService {

  Flux<Flashcard> search(String owner, String text);

  //TODO(Damian.Szwed) chyba reindex caly?
}
