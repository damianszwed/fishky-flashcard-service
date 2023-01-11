package com.github.damianszwed.fishky.flashcard.service.adapter.search.elastic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.github.damianszwed.fishky.flashcard.service.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolder;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolderService;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardSearchService;
import java.util.List;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

class ElasticSearchFlashcardSearchServiceTest {

  private static final String OWNER = "owner";
  private static final String TEXT = "text";
  private static final String FLASHCARD_ID_1 = "flashcardId1";
  private static final String FLASHCARD_ID_2 = "flashcardId2";
  private static final String FLASHCARD_ID_3 = "flashcardId3";
  private static final String FLASHCARD_FOLDER_ID_1 = "flashcardFolderId1";
  private static final String FLASHCARD_FOLDER_ID_2 = "flashcardFolderId2";

  @Test
  void search() {
    //given
    final Flashcard flashcard1 = Flashcard.builder().id(FLASHCARD_ID_1).build();
    final Flashcard flashcard2 = Flashcard.builder().id(FLASHCARD_ID_2).build();
    final Flashcard flashcard3 = Flashcard.builder().id(FLASHCARD_ID_3).build();
    final FlashcardFolderService flashcardFolderService = mock(FlashcardFolderService.class);
    final ElasticSearchFlashcardRestHighLevelClient elasticSearchFlashcardRestHighLevelClient = mock(
        ElasticSearchFlashcardRestHighLevelClient.class);
    final FlashcardSearchService flashcardSearchService = new ElasticSearchFlashcardSearchService(
        flashcardFolderService, elasticSearchFlashcardRestHighLevelClient);
    final Flux<Tuple2<String, String>> tupleFlux = Flux.create(emitter -> {
      emitter.next(Tuples.of(FLASHCARD_ID_1, FLASHCARD_FOLDER_ID_1));
      emitter.next(Tuples.of(FLASHCARD_ID_2, FLASHCARD_FOLDER_ID_2));
      emitter.next(Tuples.of(FLASHCARD_ID_3, FLASHCARD_FOLDER_ID_2));
      emitter.complete();
    });
    given(elasticSearchFlashcardRestHighLevelClient.search(OWNER, TEXT)).willReturn(tupleFlux);

    FlashcardFolder flashcardFolder1 = FlashcardFolder.builder().flashcards(List.of(flashcard1)).build();
    Mono<FlashcardFolder> flashcardFolderMono1 = Mono.just(flashcardFolder1);
    given(flashcardFolderService.getById(OWNER, FLASHCARD_FOLDER_ID_1)).willReturn(flashcardFolderMono1);

    FlashcardFolder flashcardFolder2 = FlashcardFolder.builder().flashcards(List.of(flashcard2, flashcard3)).build();
    Mono<FlashcardFolder> flashcardFolderMono2 = Mono.just(flashcardFolder2);
    given(flashcardFolderService.getById(OWNER, FLASHCARD_FOLDER_ID_2)).willReturn(flashcardFolderMono2);//TODO(Damian.Szwed) refactor

    //when
    final Flux<Flashcard> search = flashcardSearchService.search(OWNER, TEXT);

    //then
    StepVerifier.create(search)
        .expectNext(flashcard1, flashcard2, flashcard3)
        .expectComplete()
        .verify();
  }
}