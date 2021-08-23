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
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

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
                    mono.doOnNext(
                        flashcardFolder ->
                            flashcardFolderService
                                .save(ownerId, withIdAndOwner(flashcardFolder, ownerId))
                                .subscribe(newFlashcardFolder ->
                                    log.info("FlashcardFolder {} has been saved.",
                                        newFlashcardFolder.getId())))
                        .flatMap(flashcard -> accepted().build()),
                serverRequest,
                FlashcardFolder.class
            ))
        .orElse(badRequest().build());
  }

  private FlashcardFolder withIdAndOwner(FlashcardFolder flashcardFolder, String ownerId) {
    return flashcardFolder.toBuilder()
        .id(idEncoderDecoder.encodeId(ownerId, flashcardFolder.getName()))
        .owner(ownerId)
        .flashcards(
            Optional.ofNullable(flashcardFolder.getFlashcards()).orElse(Collections.emptyList()))
        .build();
  }
}
