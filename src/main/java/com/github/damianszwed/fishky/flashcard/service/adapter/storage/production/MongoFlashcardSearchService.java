package com.github.damianszwed.fishky.flashcard.service.adapter.storage.production;

import static java.util.Collections.singletonList;

import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolder;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardSearchService;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class MongoFlashcardSearchService implements FlashcardSearchService {

  private final FlashcardFolderMongoRepository flashcardFolderMongoRepository;

  public MongoFlashcardSearchService(
      FlashcardFolderMongoRepository flashcardFolderMongoRepository) {
    this.flashcardFolderMongoRepository = flashcardFolderMongoRepository;
  }

  @Override
  public Flux<FlashcardFolder> search(String owner, String text) {
    final String lowerCaseText = text != null ? text.toLowerCase() : "";
    log.info("User {} looks for flashcards containing text {}.", owner, text);
    return flashcardFolderMongoRepository.findAllByNameQuestionsOrAnswersIncludingText(
            owner, text)
        .doOnNext(flashcardFolder -> log.info("Found following folder: {} of the user: {}",
            flashcardFolder, owner))
        .filter(flashcardFolder -> flashcardFolder.getName() != null)
        .flatMap(flashcardFolder -> Flux
            .fromStream(
                Stream.concat(
                    Stream.concat(filteredByQuestion(lowerCaseText, flashcardFolder),
                        filteredByAnswers(lowerCaseText, flashcardFolder)),
                    filteredByFoldersName(lowerCaseText, flashcardFolder))))
        .doOnNext(flashcardFolder -> log.info(
            "Returning flattened folder: {} owned by {} that looked for: {}", flashcardFolder,
            owner, text));
  }

  private static Stream<FlashcardFolder> filteredByQuestion(String lowerCaseText,
      FlashcardFolder flashcardFolder) {
    return flashcardFolder.getFlashcards()
        .stream()
        .filter(flashcard -> flashcard.getQuestion() != null)
        .filter(flashcard -> flashcard.getQuestion().toLowerCase().contains(lowerCaseText))
        .map(flashcard -> flashcardFolder.toBuilder().flashcards(singletonList(flashcard))
            .build());
  }

  private static Stream<FlashcardFolder> filteredByAnswers(String lowerCaseText,
      FlashcardFolder flashcardFolder) {
    return flashcardFolder.getFlashcards()
        .stream()
        .filter(flashcard -> flashcard.getAnswers().stream()
            .anyMatch(s -> s.toLowerCase().contains(lowerCaseText)))
        .map(flashcard -> flashcardFolder.toBuilder().flashcards(singletonList(flashcard))
            .build());
  }

  private static Stream<FlashcardFolder> filteredByFoldersName(String lowerCaseText,
      FlashcardFolder flashcardFolder) {
    return flashcardFolder.getName().toLowerCase().contains(lowerCaseText) ?
        flashcardFolder.getFlashcards()
            .stream()
            .map(flashcard -> flashcardFolder.toBuilder()
                .flashcards(singletonList(flashcard))
                .build()) : Stream.empty();
  }

  @Override
  public Mono<Void> reindex() {
    log.warn("Reindex for MongoDB is not needed.");
    return Mono.empty();
  }
}
