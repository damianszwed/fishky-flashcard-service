package com.github.damianszwed.fishky.flashcard.service.adapter.storage.mongo;

import static java.util.Collections.singletonList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.github.damianszwed.fishky.flashcard.service.adapter.storage.entity.Flashcard;
import com.github.damianszwed.fishky.flashcard.service.adapter.storage.entity.FlashcardFolder;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardSearchService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class MongoFlashcardSearchServiceTest {

  private static final String OWNER = "owner";
  private static final String TEXT = "text";
  private static final String FLASHCARD_ID_1 = "flashcardId1";
  private static final String FLASHCARD_ID_2 = "flashcardId2";
  private static final String FLASHCARD_ID_3 = "flashcardId3";
  private static final String FLASHCARD_ID_4 = "flashcardId4";
  private static final String FLASHCARD_FOLDER_ID_1 = "flashcardFolderId1";
  private static final String FLASHCARD_FOLDER_ID_2 = "flashcardFolderId2";
  private static final String FLASHCARD_FOLDER_ID_3 = "flashcardFolderId3";
  private FlashcardSearchService underTest;
  private final FlashcardFolderMongoRepository flashcardFolderMongoRepository = mock(
      FlashcardFolderMongoRepository.class);

  @BeforeEach
  void setUp() {
    underTest = new MongoFlashcardSearchService(flashcardFolderMongoRepository);
  }

  @Test
  void shouldReturnFlashcardsFoundByQuestionOrAnswers() {
    //given
    final Flashcard flashcard1 = Flashcard.builder()
        .id(FLASHCARD_ID_1)
        .question("someText")
        .build();
    final Flashcard flashcard2 = Flashcard.builder()
        .id(FLASHCARD_ID_2)
        .question("tex")
        .answers(List.of("some", "someText"))
        .build();
    final Flashcard flashcard3 = Flashcard.builder()
        .id(FLASHCARD_ID_3)
        .question("tex")
        .answers(List.of("some", "someText"))
        .build();
    final Flashcard flashcard4 = Flashcard.builder()
        .id(FLASHCARD_ID_4)
        .question("ext")
        .build();

    final FlashcardFolder flashcardFolder1 = FlashcardFolder.builder()
        .id(FLASHCARD_FOLDER_ID_1)
        .name("")
        .flashcards(List.of(flashcard1)).build();
    final FlashcardFolder flashcardFolder2 = FlashcardFolder.builder()
        .id(FLASHCARD_FOLDER_ID_2)
        .name("")
        .flashcards(List.of(flashcard2, flashcard3, flashcard4)).build();
    final FlashcardFolder flashcardFolder3 = FlashcardFolder.builder()
        .name("")
        .id(FLASHCARD_FOLDER_ID_3)
        .flashcards(List.of(flashcard4)).build();

    given(flashcardFolderMongoRepository.findAllByNameQuestionsOrAnswersIncludingText(OWNER, TEXT))
        .willReturn(Flux.create(
            emitter -> {
              emitter.next(flashcardFolder1);
              emitter.next(flashcardFolder2);
              emitter.next(flashcardFolder3);
              emitter.complete();
            }));

    final FlashcardFolder expectedFolder1 = flashcardFolder1.toBuilder()
        .flashcards(singletonList(flashcard1)).build();
    final FlashcardFolder expectedFolder2a = flashcardFolder2.toBuilder()
        .flashcards(singletonList(flashcard2)).build();
    final FlashcardFolder expectedFolder2b = flashcardFolder2.toBuilder()
        .flashcards(singletonList(flashcard3)).build();

    //when
    final Flux<FlashcardFolder> searchResult = underTest.search(OWNER, TEXT);

    //then
    StepVerifier.create(searchResult)
        .expectNext(expectedFolder1, expectedFolder2a, expectedFolder2b)
        .expectComplete()
        .verify();
  }

  @Test
  void shouldReturnFlashcardsFoundByFolderName() {
    //given
    final Flashcard flashcard1 = Flashcard.builder().id(FLASHCARD_ID_1).build();
    final Flashcard flashcard2 = Flashcard.builder().id(FLASHCARD_ID_2).build();
    final Flashcard flashcard3 = Flashcard.builder().id(FLASHCARD_ID_3)
        .build();
    final Flashcard flashcard4 = Flashcard.builder().id(FLASHCARD_ID_4).build();

    final FlashcardFolder flashcardFolder1 = FlashcardFolder.builder()
        .name("ext")
        .flashcards(List.of(flashcard1)).build();
    final FlashcardFolder flashcardFolder2 = FlashcardFolder.builder()
        .name("someText")
        .flashcards(List.of(flashcard2, flashcard3, flashcard4)).build();
    final FlashcardFolder flashcardFolder3 = FlashcardFolder.builder()
        .name("TEXT")
        .flashcards(List.of(flashcard4)).build();

    given(flashcardFolderMongoRepository.findAllByNameQuestionsOrAnswersIncludingText(OWNER, TEXT))
        .willReturn(Flux.create(
            emitter -> {
              emitter.next(flashcardFolder1);
              emitter.next(flashcardFolder2);
              emitter.next(flashcardFolder3);
              emitter.complete();
            }));

    final FlashcardFolder expectedFolder1 = flashcardFolder2.toBuilder()
        .flashcards(singletonList(flashcard2)).build();
    final FlashcardFolder expectedFolder2 = flashcardFolder2.toBuilder()
        .flashcards(singletonList(flashcard3)).build();
    final FlashcardFolder expectedFolder3 = flashcardFolder2.toBuilder()
        .flashcards(singletonList(flashcard4)).build();
    final FlashcardFolder expectedFolder4 = flashcardFolder3.toBuilder()
        .flashcards(singletonList(flashcard4)).build();

    //when
    final Flux<FlashcardFolder> searchResult = underTest.search(OWNER, TEXT);

    //then
    StepVerifier.create(searchResult)
        .expectNext(expectedFolder1, expectedFolder2, expectedFolder3, expectedFolder4)
        .expectComplete()
        .verify();
  }

  @Test
  void shouldReturnEmptyResultOnEmptySearch() {
    //given
    given(flashcardFolderMongoRepository.findAllByNameQuestionsOrAnswersIncludingText(OWNER,
        TEXT)).willReturn(Flux.empty());

    //when
    final Flux<FlashcardFolder> searchResult = underTest.search(OWNER, TEXT);

    //then
    StepVerifier.create(searchResult)
        .expectComplete()
        .verify();
  }
}