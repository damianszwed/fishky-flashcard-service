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
  private final ElasticSearchFlashcardRestHighLevelClient elasticSearchFlashcardRestHighLevelClient;

  public ElasticSearchFlashcardSearchService(FlashcardFolderService flashcardFolderStorage,
      ElasticSearchFlashcardRestHighLevelClient elasticSearchFlashcardRestHighLevelClient) {
    this.flashcardFolderStorage = flashcardFolderStorage;
    this.elasticSearchFlashcardRestHighLevelClient = elasticSearchFlashcardRestHighLevelClient;
  }

  @Override
  public Flux<Flashcard> search(String owner, String text) {
    return elasticSearchFlashcardRestHighLevelClient.search(owner, text)
        .map(tuples1 -> getFolderFromMainDatabase(owner, tuples1))
        .map(this::logFlashcardFolder)
        .flatMap(this::withFlashcards)
        .flatMap(this::filteredFlashcardById);
  }

  @Override
  public Mono<Boolean> reindex() {
    final Flux<FlashcardFolder> flashcardFolderFlux = flashcardFolderStorage.get();
    //TODO(Damian.Szwed) impl
    return elasticSearchFlashcardRestHighLevelClient.reindex();
  }

  private Tuple2<String, Mono<FlashcardFolder>> getFolderFromMainDatabase(
      String owner,
      Tuple2<String, String> flashcardIdAndFlashcardFolder) {
    final String flashcardId = flashcardIdAndFlashcardFolder.getT1();
    final String flashcardFolderId = flashcardIdAndFlashcardFolder.getT2();
    log.info("Will take from main database flashcard with id: {} from folder: {} for user {}",
        flashcardId, flashcardFolderId, owner);
    return Tuples.of(flashcardId, flashcardFolderStorage.getById(owner, flashcardFolderId));
  }

  private Tuple2<String, Mono<FlashcardFolder>> logFlashcardFolder(
      Tuple2<String, Mono<FlashcardFolder>> flashcardIdAndFlashcardFolder) {
    return Tuples.of(flashcardIdAndFlashcardFolder.getT1(), flashcardIdAndFlashcardFolder.getT2()
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
      Tuple2<String, Mono<FlashcardFolder>> flashcardIdAndFlashcardFolder) {
    return flashcardIdAndFlashcardFolder.getT2().map(
        flashcardFolder -> Tuples.of(flashcardIdAndFlashcardFolder.getT1(),
            flashcardFolder.getFlashcards()));
  }

  private Flux<Flashcard> filteredFlashcardById(
      Tuple2<String, List<Flashcard>> flashcardIdAndFlashcards) {
    return Flux.fromStream(flashcardIdAndFlashcards.getT2().stream()
        .filter(flashcard -> flashcardIdAndFlashcards.getT1().equals(flashcard.getId())));
  }
}
