package com.github.damianszwed.fishky.flashcard.service.adapter.storage.elastic;

import com.github.damianszwed.fishky.flashcard.service.adapter.storage.entity.Flashcard;
import com.github.damianszwed.fishky.flashcard.service.adapter.storage.entity.FlashcardFolder;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolderStorage;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardSearchService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Slf4j
public class ElasticSearchFlashcardSearchService implements FlashcardSearchService {

  /**
   * By this field messages are indexing one by one. More information: <a
   * href="https://stackoverflow.com/questions/62837058/can-flux-of-project-reactor-process-messages-one-by-one">stackoverflow</a>
   */
  private static final int CONCURRENCY = 1;
  private final FlashcardFolderStorage flashcardFolderStorage;
  private final ElasticSearchFlashcardRestHighLevelClient elasticSearchFlashcardRestHighLevelClient;

  public ElasticSearchFlashcardSearchService(FlashcardFolderStorage flashcardFolderStorage,
      ElasticSearchFlashcardRestHighLevelClient elasticSearchFlashcardRestHighLevelClient) {
    this.flashcardFolderStorage = flashcardFolderStorage;
    this.elasticSearchFlashcardRestHighLevelClient = elasticSearchFlashcardRestHighLevelClient;
  }

  @Override
  public Flux<FlashcardFolder> search(String owner, String text) {
    return elasticSearchFlashcardRestHighLevelClient.search(owner, text)
        .map(tuples -> getFolderFromMainDatabase(owner, tuples))
        .map(this::logFlashcardFolder)
        .flatMap(this::filteredFlashcardById);
  }

  @Override
  public Mono<Void> reindex() {
    log.info("Invoked reindex().");
    return elasticSearchFlashcardRestHighLevelClient
        .erase()
        .flatMapMany(unused -> flashcardFolderStorage.get())
        .flatMap(flashcardFolder -> {
          log.info("Incoming folder {} from main database.", flashcardFolder);
          return Flux.fromStream(
              flashcardFolder.getFlashcards()
                  .stream()
                  .map(flashcard -> Tuples.of(flashcardFolder, flashcard)));
        })
        .flatMap(objects -> {
          final FlashcardFolder flashcardFolder = objects.getT1();
          final Flashcard flashcard = objects.getT2();
          log.info("Will index following flashcard {}", flashcard);
          return elasticSearchFlashcardRestHighLevelClient.indexFlashcard(
              flashcardFolder,
              flashcard);
        }, CONCURRENCY)
        .then();
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

  private Mono<FlashcardFolder> filteredFlashcardById(
      Tuple2<String, Mono<FlashcardFolder>> flashcardIdAndFlashcardFolder) {
    return flashcardIdAndFlashcardFolder
        .getT2()
        .map(flashcardFolder -> flashcardFolder
            .toBuilder()
            .flashcards(getFilteredFlashcards(flashcardIdAndFlashcardFolder, flashcardFolder))
            .build());
  }

  private static List<Flashcard> getFilteredFlashcards(
      Tuple2<String, Mono<FlashcardFolder>> flashcardIdAndFlashcardFolder,
      FlashcardFolder flashcardFolder) {
    return flashcardFolder.getFlashcards().stream()
        .filter(flashcard -> flashcardIdAndFlashcardFolder.getT1().equals(flashcard.getId()))
        .collect(
            Collectors.toList());
  }
}
