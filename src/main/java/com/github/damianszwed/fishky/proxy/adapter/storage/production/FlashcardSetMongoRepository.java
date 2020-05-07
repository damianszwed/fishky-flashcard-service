package com.github.damianszwed.fishky.proxy.adapter.storage.production;

import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardSet;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface FlashcardSetMongoRepository extends
    ReactiveMongoRepository<FlashcardSet, String> {

  Mono<FlashcardSet> findByName(String name);
}
