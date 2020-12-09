package com.github.damianszwed.fishky.proxy.port.flashcard;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FlashcardFolderService {

  Flux<FlashcardFolder> get(String owner);

  Mono<FlashcardFolder> get(String owner, String flashcardFolderName);

  Mono<FlashcardFolder> getById(String owner, String flashcardFolderId);

  void save(String owner, FlashcardFolder flashcardFolder);

  void remove(String owner, String flashcardFolderId);
}