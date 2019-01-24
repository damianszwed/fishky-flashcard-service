package com.github.damianszwed.fishky.proxy.adapter.storage.production;

import com.github.damianszwed.fishky.proxy.port.flashcard.Flashcard;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface FlashcardMongoRepository extends ReactiveMongoRepository<Flashcard, String> {

}
