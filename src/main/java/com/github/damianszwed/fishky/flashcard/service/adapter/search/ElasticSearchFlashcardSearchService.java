package com.github.damianszwed.fishky.flashcard.service.adapter.search;

import com.github.damianszwed.fishky.flashcard.service.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardSearchService;
import reactor.core.publisher.Flux;

public class ElasticSearchFlashcardSearchService implements FlashcardSearchService {

  @Override
  public Flux<Flashcard> search(String owner, String text) {
    //TODO(Damian.Szwed) all the thing...
    return null;
  }
}
