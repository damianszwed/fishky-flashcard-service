package com.github.damianszwed.fishky.flashcard.service.business;

import static org.springframework.web.reactive.function.server.ServerResponse.accepted;
import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

import com.github.damianszwed.fishky.flashcard.service.port.CommandQueryHandler;
import com.github.damianszwed.fishky.flashcard.service.port.IdEncoderDecoder;
import com.github.damianszwed.fishky.flashcard.service.port.OwnerProvider;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolder;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolderService;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
              .flatMap(flashcardFolderToCopy -> mergeAndCopyFlashcardFolder(userId,
                  flashcardFolderToCopy))
              .switchIfEmpty(badRequestFolderToCopyNotFound(ownerId, folderIdToCopy));
        })
        .switchIfEmpty(badRequest().build());
  }

  private Mono<ServerResponse> mergeAndCopyFlashcardFolder(
      final String userId,
      final FlashcardFolder flashcardFolderToCopy) {
    return flashcardFolderService
        .get(userId, flashcardFolderToCopy.getName())
        .flatMap(flashcardFolder -> mergeAndSaveFlashcardFolders(userId, flashcardFolderToCopy,
            flashcardFolder))
        .switchIfEmpty(saveFlashcardFolder(userId, flashcardFolderToCopy.toBuilder()
            .id(idEncoderDecoder.encodeId(userId, flashcardFolderToCopy.getName()))
            .owner(userId)
            .build()));
  }

  private Mono<ServerResponse> mergeAndSaveFlashcardFolders(
      final String userId,
      final FlashcardFolder flashcardFolderToCopy,
      final FlashcardFolder flashcardFolder) {
    return saveFlashcardFolder(userId, flashcardFolder.toBuilder()
        .flashcards(Stream.concat(flashcardFolder.getFlashcards().stream(),
            flashcardFolderToCopy.getFlashcards().stream())
            .collect(Collectors.toList()))
        .build());
  }

  private Mono<ServerResponse> saveFlashcardFolder(
      final String userId,
      final FlashcardFolder flashcardFolderToSave) {
    return flashcardFolderService
        .save(userId, flashcardFolderToSave
            .toBuilder()
            .flashcards(flashcardFolderToSave.getFlashcards().stream().map(
                flashcard -> flashcard.toBuilder()
                    .id(idEncoderDecoder.encodeId(userId, flashcard.getQuestion()))
                    .build()
            ).collect(Collectors.toList()))
            .build())
        .flatMap(p -> accepted().build())
        .switchIfEmpty(status(HttpStatus.INTERNAL_SERVER_ERROR).build());
  }

  private Mono<ServerResponse> badRequestFolderToCopyNotFound(
      final String ownerId,
      final String folderIdToCopy) {
    return badRequest().build().doOnNext(serverResponse -> log
        .warn("Not found folder to copy with id {} owned by {}", folderIdToCopy, ownerId));
  }
}
