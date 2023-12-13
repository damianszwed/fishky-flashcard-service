package com.github.damianszwed.fishky.flashcard.service.business;

import static org.springframework.web.reactive.function.server.ServerResponse.accepted;
import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;

import com.github.damianszwed.fishky.flashcard.service.adapter.storage.entity.Flashcard;
import com.github.damianszwed.fishky.flashcard.service.adapter.storage.entity.FlashcardFolder;
import com.github.damianszwed.fishky.flashcard.service.port.CommandQueryHandler;
import com.github.damianszwed.fishky.flashcard.service.port.IdEncoderDecoder;
import com.github.damianszwed.fishky.flashcard.service.port.OwnerProvider;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolderService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Slf4j
public class FlashcardFolderSaveFlashcardCommandHandler implements CommandQueryHandler {

  private final FlashcardFolderService flashcardFolderService;
  private final IdEncoderDecoder idEncoderDecoder;
  private final OwnerProvider ownerProvider;

  public FlashcardFolderSaveFlashcardCommandHandler(
      FlashcardFolderService flashcardFolderService,
      IdEncoderDecoder idEncoderDecoder,
      OwnerProvider ownerProvider) {
    this.flashcardFolderService = flashcardFolderService;
    this.idEncoderDecoder = idEncoderDecoder;
    this.ownerProvider = ownerProvider;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    return ownerProvider.provide(serverRequest)
        .map(ownerId -> serverRequest.bodyToMono(Flashcard.class)
            .map(flashcardToSave -> withFlashcardFolder(serverRequest, ownerId, flashcardToSave))
            .flatMap(tuples -> saveFolderWithNewFlashcard(ownerId, tuples))
            .doOnNext(flashcardFolder -> log.info("FlashcardFolder {} has been saved.",
                flashcardFolder.getId()))
            .flatMap(flashcard -> accepted().build()))
        .orElse(badRequest().build());
  }

  private Tuple2<Flashcard, Mono<FlashcardFolder>> withFlashcardFolder(ServerRequest serverRequest,
      String ownerId, Flashcard flashcardToSave) {
    return Tuples.of(flashcardToSave, flashcardFolderService
        .getById(ownerId,
            serverRequest.pathVariable("id")));
  }

  private Mono<FlashcardFolder> saveFolderWithNewFlashcard(String ownerId,
      Tuple2<Flashcard, Mono<FlashcardFolder>> tuples) {
    return tuples.getT2()
        .flatMap(flashcardFolder -> {
          Flashcard flashcardToSave = tuples.getT1();
          String id = idEncoderDecoder
              .encodeId(ownerId,
                  flashcardToSave.getQuestion());
          List<Flashcard> flashcardsWithOneRemoved = flashcardFolder.getFlashcards()
              .stream()
              .filter(flashcard -> !flashcard.getId().equals(id))
              .collect(Collectors.toList());
          flashcardsWithOneRemoved.add(flashcardToSave
              .toBuilder()
              .id(id)
              .build());
          return flashcardFolderService
              .save(
                  ownerId, flashcardFolder.toBuilder()
                      .flashcards(flashcardsWithOneRemoved)
                      .build());
        });
  }
}
