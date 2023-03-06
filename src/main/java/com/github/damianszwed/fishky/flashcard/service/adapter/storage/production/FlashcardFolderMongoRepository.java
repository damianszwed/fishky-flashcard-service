package com.github.damianszwed.fishky.flashcard.service.adapter.storage.production;

import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolder;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FlashcardFolderMongoRepository extends
    ReactiveMongoRepository<FlashcardFolder, String> {

  Mono<FlashcardFolder> findByOwnerAndName(String owner, String name);

  Flux<FlashcardFolder> findAllByOwner(String owner);

  @Query("{'owner': ?0, '$or' : ["
      + "{'name': { $regex:  ?1, $options:  'i'}}, "
      + "{'flashcards.question': {$regex:  ?1, $options:  'i'}}, "
      + "{'flashcards.answers': {$regex:  ?1, $options:  'i'}}"
      + "]}")
  Flux<FlashcardFolder> findAllByCustomQuery(String owner, String text);
}
