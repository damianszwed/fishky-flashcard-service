package com.github.damianszwed.fishky.flashcard.service.adapter.storage.production;

import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolder;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardSearchService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class MongoFlashcardSearchService implements FlashcardSearchService {

  private final FlashcardFolderMongoRepository flashcardFolderMongoRepository;

  public MongoFlashcardSearchService(FlashcardFolderMongoRepository flashcardFolderMongoRepository) {
    this.flashcardFolderMongoRepository = flashcardFolderMongoRepository;
  }

  @Override
  public Flux<FlashcardFolder> search(String owner, String text) {
    return flashcardFolderMongoRepository.findAllByNameQuestionsOrAnswersIncludingText(owner, text);
    //TODO operations to flat answers and unit tests
  }

  @Override
  public Mono<Void> reindex() {
    log.warn("Reindex for MongoDB is not needed.");
    return Mono.empty();
  }
}
