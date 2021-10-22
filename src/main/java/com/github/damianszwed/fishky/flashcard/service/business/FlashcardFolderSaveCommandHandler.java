package com.github.damianszwed.fishky.flashcard.service.business;

import static org.springframework.web.reactive.function.server.ServerResponse.accepted;
import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;

import com.github.damianszwed.fishky.flashcard.service.port.CommandQueryHandler;
import com.github.damianszwed.fishky.flashcard.service.port.IdEncoderDecoder;
import com.github.damianszwed.fishky.flashcard.service.port.OwnerProvider;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolder;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolderService;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Slf4j
public class FlashcardFolderSaveCommandHandler implements CommandQueryHandler {

  private final FlashcardFolderService flashcardFolderService;
  private final IdEncoderDecoder idEncoderDecoder;
  private final OwnerProvider ownerProvider;
  private final ValidatorRequestHandler validatorRequestHandler;

  public FlashcardFolderSaveCommandHandler(
      FlashcardFolderService flashcardFolderService,
      IdEncoderDecoder idEncoderDecoder,
      OwnerProvider ownerProvider,
      ValidatorRequestHandler validatorRequestHandler) {
    this.flashcardFolderService = flashcardFolderService;
    this.idEncoderDecoder = idEncoderDecoder;
    this.ownerProvider = ownerProvider;
    this.validatorRequestHandler = validatorRequestHandler;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    return ownerProvider.provide(serverRequest)
        .map(ownerId ->
            validatorRequestHandler.requireValidBody(
                mono ->
                    mono
                        .map(flashcardFolder -> Tuples.of(flashcardFolder,
                            flashcardFolderService.get(ownerId, flashcardFolder.getName())))
                        .flatMap(this::promoteFolderWithFlashcards)
                        .flatMap(flashcardFolder -> flashcardFolderService
                            .save(ownerId, withIdAndOwner(flashcardFolder, ownerId)))
                        .doOnNext(flashcardFolder -> log.info("FlashcardFolder {} has been saved.",
                            flashcardFolder.getId()))
                        .flatMap(flashcardFolder -> accepted().build()),
                serverRequest,
                FlashcardFolder.class
            ))
        .orElse(badRequest().build());
  }

  /**
   * Returns flashcard folder from request if it has any flashcards. Otherwise returns existing
   * flashcard folder if it exists.
   *
   * @param tuples Tuple2
   * @return FlashcardFolder
   */
  private Mono<FlashcardFolder> promoteFolderWithFlashcards(
      Tuple2<FlashcardFolder, Mono<FlashcardFolder>> tuples) {
    return tuples.getT2()
        .map(existingFlashcardFolder -> Optional.ofNullable(tuples.getT1().getFlashcards())
            .filter(flashcards -> !flashcards.isEmpty())
            .map(flashcards -> tuples.getT1())
            .orElse(existingFlashcardFolder)).defaultIfEmpty(tuples.getT1());
  }

  private FlashcardFolder withIdAndOwner(FlashcardFolder flashcardFolder, String ownerId) {
    return flashcardFolder.toBuilder()
        .id(idEncoderDecoder.encodeId(ownerId, flashcardFolder.getName()))
        .owner(ownerId)
        .flashcards(
            Optional.ofNullable(flashcardFolder.getFlashcards())
                .map(flashcards -> flashcards.stream()
                    .map(flashcard -> flashcard.toBuilder()
                        .id(idEncoderDecoder.encodeId(ownerId, flashcard.getQuestion()))
                        .build())
                    .collect(Collectors.toList()))
                .orElse(Collections.emptyList()))
        .build();
  }
}
