package com.github.damianszwed.fishky.proxy.adapter.storage.production;

import com.github.damianszwed.fishky.proxy.port.flashcard.Flashcard;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FlashcardRepository extends MongoRepository<Flashcard, String> {
}
