package com.github.damianszwed.fishky.flashcard.service.business;

import static org.springframework.web.reactive.function.server.ServerResponse.accepted;

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
    return validatorRequestHandler
        .requireValidBody(
            mono -> mono.doOnNext(
                flashcardFolder -> flashcardFolderService
                    .save(ownerProvider.provide(serverRequest),
                        withIdAndOwner(flashcardFolder, serverRequest))
                    .subscribe(newFlashcardFolder ->
                        log.info("FlashcardFolder {} has been saved.",
                            newFlashcardFolder.getId())))
                .flatMap(flashcard -> accepted().build()),
            serverRequest,
            FlashcardFolder.class
        );
  }

  private FlashcardFolder withIdAndOwner(
      FlashcardFolder flashcardFolder,
      ServerRequest serverRequest) {
    return flashcardFolder.toBuilder()
        .id(idEncoderDecoder
            .encodeId(ownerProvider.provide(serverRequest), flashcardFolder.getName()))
        .owner(ownerProvider.provide(serverRequest))
        .flashcards(
            Optional.ofNullable(flashcardFolder.getFlashcards()).orElse(Collections.emptyList()))
        .build();
  }
}
