package com.github.damianszwed.fishky.proxy.port.flashcard;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FlashcardGroupStorage {

  Flux<FlashcardGroup> get(String owner);

  Mono<FlashcardGroup> get(String owner, String name);//TODO(Damian.Szwed) rename name to groupName

  Mono<FlashcardGroup> getById(String id);//TODO(Damian.Szwed) rename to groupId

  void save(FlashcardGroup flashcardGroup);

  void remove(String id);//TODO(Damian.Szwed) rename to groupId
}