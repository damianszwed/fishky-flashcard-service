package com.github.damianszwed.fishky.flashcard.service.adapter.search.elastic;

import static org.mockito.BDDMockito.given;

import com.github.damianszwed.fishky.flashcard.service.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolder;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolderService;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardSearchService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@ExtendWith(MockitoExtension.class)
class ElasticSearchFlashcardSearchServiceTest {

  private static final String OWNER = "owner";
  private static final String TEXT = "text";
  private static final String FLASHCARD_ID_1 = "flashcardId1";
  private static final String FLASHCARD_ID_2 = "flashcardId2";
  private static final String FLASHCARD_ID_3 = "flashcardId3";
  private static final String FLASHCARD_FOLDER_ID_1 = "flashcardFolderId1";
  private static final String FLASHCARD_FOLDER_ID_2 = "flashcardFolderId2";
  @Mock
  private FlashcardFolderService flashcardFolderService;
  @Mock
  private ElasticSearchFlashcardRestHighLevelClient elasticSearchFlashcardRestHighLevelClient;
  private FlashcardSearchService underTest;

  @BeforeEach
  void setUp() {
    underTest = new ElasticSearchFlashcardSearchService(
        flashcardFolderService, elasticSearchFlashcardRestHighLevelClient);
  }

  @Test
  void shouldReturnThreeFlashcardsOnSimpleSearch() {
    //given
    final Flashcard flashcard1 = Flashcard.builder().id(FLASHCARD_ID_1).build();
    final Flashcard flashcard2 = Flashcard.builder().id(FLASHCARD_ID_2).build();
    final Flashcard flashcard3 = Flashcard.builder().id(FLASHCARD_ID_3).build();
    final Flux<Tuple2<String, String>> flashcardIdAndFlashcardFolderTuples = Flux.create(
        emitter -> {
          emitter.next(Tuples.of(FLASHCARD_ID_1, FLASHCARD_FOLDER_ID_1));
          emitter.next(Tuples.of(FLASHCARD_ID_2, FLASHCARD_FOLDER_ID_2));
          emitter.next(Tuples.of(FLASHCARD_ID_3, FLASHCARD_FOLDER_ID_2));
          emitter.complete();
        });
    given(elasticSearchFlashcardRestHighLevelClient.search(OWNER, TEXT)).willReturn(
        flashcardIdAndFlashcardFolderTuples);

    final FlashcardFolder flashcardFolder1 = FlashcardFolder.builder()
        .flashcards(List.of(flashcard1)).build();
    given(flashcardFolderService.getById(OWNER, FLASHCARD_FOLDER_ID_1)).willReturn(
        Mono.just(flashcardFolder1));

    final FlashcardFolder flashcardFolder2 = FlashcardFolder.builder()
        .flashcards(List.of(flashcard2, flashcard3)).build();
    given(flashcardFolderService.getById(OWNER, FLASHCARD_FOLDER_ID_2)).willReturn(
        Mono.just(flashcardFolder2));

    //when
    final Flux<Flashcard> searchResult = underTest.search(OWNER, TEXT);

    //then
    StepVerifier.create(searchResult)
        .expectNext(flashcard1, flashcard2, flashcard3)
        .expectComplete()
        .verify();
  }

  @Test
  void shouldReturnEmptyResultOnEmptySearch() {
    //given
    given(elasticSearchFlashcardRestHighLevelClient.search(OWNER, TEXT)).willReturn(Flux.empty());

    //when
    final Flux<Flashcard> searchResult = underTest.search(OWNER, TEXT);

    //then
    StepVerifier.create(searchResult)
        .expectComplete()
        .verify();
  }

  @Test
  void shouldEmitErrorOnFirstException() {
    //given
    final Flux<Tuple2<String, String>> flashcardIdAndFlashcardFolderTuples = Flux.create(
        emitter -> {
          emitter.next(Tuples.of(FLASHCARD_ID_1, FLASHCARD_FOLDER_ID_1));
          emitter.next(Tuples.of(FLASHCARD_ID_2, FLASHCARD_FOLDER_ID_2));
          emitter.next(Tuples.of(FLASHCARD_ID_3, FLASHCARD_FOLDER_ID_2));
          emitter.complete();
        });
    given(elasticSearchFlashcardRestHighLevelClient.search(OWNER, TEXT)).willReturn(
        flashcardIdAndFlashcardFolderTuples);

    given(flashcardFolderService.getById(OWNER, FLASHCARD_FOLDER_ID_1)).willReturn(
        Mono.error(new RuntimeException("Mocked exception")));

    //when
    final Flux<Flashcard> searchResult = underTest.search(OWNER, TEXT);

    //then
    StepVerifier.create(searchResult)
        .expectError()
        .verify();
  }

  @Test
  void shouldEmitFirstAndSecondFlashcardsAndThenEmitAnErrorOnException() {
    //given
    final Flashcard flashcard1 = Flashcard.builder().id(FLASHCARD_ID_1).build();
    final Flux<Tuple2<String, String>> flashcardIdAndFlashcardFolderTuples = Flux.create(
        emitter -> {
          emitter.next(Tuples.of(FLASHCARD_ID_1, FLASHCARD_FOLDER_ID_1));
          emitter.next(Tuples.of(FLASHCARD_ID_2, FLASHCARD_FOLDER_ID_2));
          emitter.next(Tuples.of(FLASHCARD_ID_3, FLASHCARD_FOLDER_ID_2));
          emitter.complete();
        });
    given(elasticSearchFlashcardRestHighLevelClient.search(OWNER, TEXT)).willReturn(
        flashcardIdAndFlashcardFolderTuples);

    final FlashcardFolder flashcardFolder1 = FlashcardFolder.builder()
        .flashcards(List.of(flashcard1)).build();
    given(flashcardFolderService.getById(OWNER, FLASHCARD_FOLDER_ID_1)).willReturn(
        Mono.just(flashcardFolder1));

    given(flashcardFolderService.getById(OWNER, FLASHCARD_FOLDER_ID_2)).willReturn(
        Mono.error(new RuntimeException("Mocked exception")));

    //when
    final Flux<Flashcard> searchResult = underTest.search(OWNER, TEXT);

    //then
    StepVerifier.create(searchResult)
        .expectNext(flashcard1)
        .expectError()
        .verify();
  }
}