package com.github.damianszwed.fishky.flashcard.service.adapter.search.elastic;

import com.github.damianszwed.fishky.flashcard.service.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolder;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolderService;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardSearchService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Slf4j
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
        .map(tuples1 -> getFolderFromMainDatabase(owner, tuples1))
        .map(this::logFlashcardFolder)
        .flatMap(this::withFlashcards)
        .flatMap(this::filteredFlashcardFlux);
  }

  private Tuple2<String, Mono<FlashcardFolder>> getFolderFromMainDatabase(
      String owner,
      Tuple2<String, String> flashcardIdAndFolder) {
    final String flashcardId = flashcardIdAndFolder.getT1();
    final String flashcardFolderId = flashcardIdAndFolder.getT2();
    log.info("Will take from main database flashcard with id: {} from folder: {} for user {}",
        flashcardId, flashcardFolderId, owner);
    return Tuples.of(flashcardId, flashcardFolderStorage.getById(owner, flashcardFolderId));
  }

  private Tuple2<String, Mono<FlashcardFolder>> logFlashcardFolder(
      Tuple2<String, Mono<FlashcardFolder>> tuples1) {
    return Tuples.of(tuples1.getT1(), tuples1.getT2()
        .doOnSuccess(flashcardFolder -> {
          if (flashcardFolder == null) {
            log.info("Synchronization issue - flashcard folder mentioned earlier "
                + "doesn't exist in the main database.");
          } else {
            log.info("Got {} folder from main database.", flashcardFolder.getId());
          }
        }));
  }

  private Mono<Tuple2<String, List<Flashcard>>> withFlashcards(
      Tuple2<String, Mono<FlashcardFolder>> tuples1) {
    return tuples1.getT2().map(
        flashcardFolder -> Tuples.of(tuples1.getT1(), flashcardFolder.getFlashcards()));
  }

  private Flux<Flashcard> filteredFlashcardFlux(Tuple2<String, List<Flashcard>> tuples) {
    return Flux.fromStream(tuples.getT2().stream()
        .filter(flashcard -> tuples.getT1().equals(flashcard.getId())));
  }
}
