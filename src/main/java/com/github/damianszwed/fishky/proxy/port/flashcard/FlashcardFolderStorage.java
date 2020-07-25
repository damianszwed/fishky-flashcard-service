package com.github.damianszwed.fishky.proxy.port.flashcard;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FlashcardFolderStorage {

  Flux<FlashcardFolder> get(String owner);

  Mono<FlashcardFolder> get(String owner,
      String name);//TODO(Damian.Szwed) rename name to flashcardFolderName

  Mono<FlashcardFolder> getById(String id);//TODO(Damian.Szwed) rename to flashcardFolderId

  void save(FlashcardFolder flashcardFolder);

  void remove(String id);//TODO(Damian.Szwed) rename to flashcardFolderId
}