package com.github.damianszwed.fishky.flashcard.service.adapter.search.elastic;

import com.github.damianszwed.fishky.flashcard.service.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolderService;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardSearchService;
import java.util.stream.Collectors;
import reactor.core.publisher.Flux;

public class ElasticSearchFlashcardSearchService implements FlashcardSearchService {

  private final FlashcardFolderService flashcardFolderStorage;
  private final ElasticSearchRename elasticSearchRename;

  public ElasticSearchFlashcardSearchService(FlashcardFolderService flashcardFolderStorage,
      ElasticSearchRename elasticSearchRename) {
    this.flashcardFolderStorage = flashcardFolderStorage;
    this.elasticSearchRename = elasticSearchRename;
  }

  @Override
  public Flux<Flashcard> search(String owner, String text) {
    return elasticSearchRename.search(owner, text)
        .flatMap(objects -> {
          final String flashcardId = objects.getT1();
          final String flashcardFolderId = objects.getT2();
          return flashcardFolderStorage.getById(owner, flashcardFolderId)
              .flatMapIterable(
                  flashcardFolder -> flashcardFolder
                      .getFlashcards().stream()
                      .filter(flashcard ->
                          flashcardId.equals(flashcard.getId()))
                      .collect(Collectors.toList()));
        });
  }
}
