package com.github.damianszwed.fishky.proxy.adapter.storage.production;

import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardGroup;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface FlashcardGroupMongoRepository extends
    ReactiveMongoRepository<FlashcardGroup, String> {

  Mono<FlashcardGroup> findByName(String name);
}
