package com.github.damianszwed.fishky.flashcard.service.adapter.search.elastic;

import com.github.damianszwed.fishky.flashcard.service.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolder;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolderService;
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

  private final FlashcardFolderService flashcardFolderStorage;
  private final ElasticSearchRename elasticSearchRename;

  public ElasticSearchFlashcardSearchService(FlashcardFolderService flashcardFolderStorage,
      ElasticSearchRename elasticSearchRename) {
    this.flashcardFolderStorage = flashcardFolderStorage;
    this.elasticSearchRename = elasticSearchRename;
  }

  @Override
  public Flux<Flashcard> search(String owner, String text) {
    Mono<List<Tuple2<String, String>>> search = elasticSearchRename.search(owner, text);
    Flux<Tuple2<String, String>> tuple2Flux = search.flatMapIterable(s -> s);
    Flux<Tuple2<String, Mono<FlashcardFolder>>> map1 = tuple2Flux.map(s -> {
      final String flashcardId = s.getT1();
      final String flashcardFolderId = s.getT2();
      log.info("Will take flashcard with id: {} from folder: {} for user {}", flashcardId,
          flashcardFolderId, owner);
      Mono<FlashcardFolder> byId = flashcardFolderStorage.getById(owner, flashcardFolderId);
      return Tuples.of(flashcardId, byId);
    });
    Flux<Tuple2<String, Mono<FlashcardFolder>>> map = map1.map(abc -> {
      return Tuples.of(abc.getT1(), abc.getT2().doOnSuccess(flashcardFolder -> {
        if (flashcardFolder == null) {
          log.info(
              "Synchronization issue - flashcard folder mentioned earlier doesn't exist in main database.");
        } else {
          log.info("Got {} folder from main database.", flashcardFolder.getId());
        }
      }));
    });
    Flux<Tuple2<String, Mono<List<Flashcard>>>> map2 = map.map(abc -> {
      return Tuples.of(abc.getT1(), abc.getT2().map(FlashcardFolder::getFlashcards));
    });

    Flux<Flux<Flashcard>> map3 = map2.map(tuplesWithListMono -> {
      return tuplesWithListMono.getT2().flatMapIterable(flashcards -> flashcards.stream()
          .filter(flashcard -> tuplesWithListMono.getT1().equals(flashcard.getId()))
          .collect(
              Collectors.toList()));
    });
    Flux<Flashcard> flashcardFlux = map3.flatMap(s -> s);
    return flashcardFlux;
  }
}
