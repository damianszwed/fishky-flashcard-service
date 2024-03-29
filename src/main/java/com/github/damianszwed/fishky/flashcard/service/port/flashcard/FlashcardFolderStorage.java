package com.github.damianszwed.fishky.flashcard.service.port.flashcard;

import com.github.damianszwed.fishky.flashcard.service.adapter.storage.entity.FlashcardFolder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FlashcardFolderStorage {

  /**
   * Used only for search reindexing.
   *
   * @return FlashcardFolders
   */
  Flux<FlashcardFolder> get();

  Flux<FlashcardFolder> get(String owner);

  Mono<FlashcardFolder> get(String owner, String flashcardFolderName);

  Mono<FlashcardFolder> getById(String owner, String flashcardFolderId);

  Mono<FlashcardFolder> save(String owner, FlashcardFolder flashcardFolder);

  Mono<Void> remove(String owner, String flashcardFolderId);
}