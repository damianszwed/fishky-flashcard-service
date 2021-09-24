package com.github.damianszwed.fishky.flashcard.service.business;

import static org.springframework.web.reactive.function.server.ServerResponse.accepted;
import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

import com.github.damianszwed.fishky.flashcard.service.port.CommandQueryHandler;
import com.github.damianszwed.fishky.flashcard.service.port.IdEncoderDecoder;
import com.github.damianszwed.fishky.flashcard.service.port.OwnerProvider;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
public class CopyFolderCommandHandler implements CommandQueryHandler {

  private final FlashcardFolderService flashcardFolderService;
  private final IdEncoderDecoder idEncoderDecoder;
  private final OwnerProvider ownerProvider;

  public CopyFolderCommandHandler(
      FlashcardFolderService flashcardFolderService,
      IdEncoderDecoder idEncoderDecoder,
      OwnerProvider ownerProvider) {
    this.flashcardFolderService = flashcardFolderService;
    this.idEncoderDecoder = idEncoderDecoder;
    this.ownerProvider = ownerProvider;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    return Mono.justOrEmpty(ownerProvider.provide(serverRequest))
        .flatMap(userId -> {
          final String ownerId = serverRequest.pathVariable("ownerId");
          final String folderIdToCopy = serverRequest.pathVariable("folderId");
          return flashcardFolderService.getById(ownerId, folderIdToCopy)
              .flatMap(flashcardFolderToCopy -> flashcardFolderService
                  .save(userId, flashcardFolderToCopy.toBuilder()
                      .id(idEncoderDecoder.encodeId(userId, flashcardFolderToCopy.getName()))
                      .owner(userId)
                      .build())
                  .flatMap(p -> accepted().build())
                  .switchIfEmpty(status(HttpStatus.INTERNAL_SERVER_ERROR).build()))
              .switchIfEmpty(badRequestFolderToCopyNotFound(ownerId, folderIdToCopy));
        })
        .switchIfEmpty(badRequest().build());
  }

  private Mono<ServerResponse> badRequestFolderToCopyNotFound(
      final String ownerId,
      final String folderIdToCopy) {
    return badRequest().build().doOnNext(serverResponse -> log
        .warn("Not found folder to copy with id {} owned by {}", folderIdToCopy, ownerId));
  }
}
