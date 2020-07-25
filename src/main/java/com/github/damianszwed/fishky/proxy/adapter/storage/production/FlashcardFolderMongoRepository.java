package com.github.damianszwed.fishky.proxy.adapter.storage.production;

import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardFolder;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FlashcardFolderMongoRepository extends
    ReactiveMongoRepository<FlashcardFolder, String> {

  Mono<FlashcardFolder> findByName(String name);

  Flux<FlashcardFolder> findAllByOwner(String owner);
}
