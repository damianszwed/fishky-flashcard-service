package com.github.damianszwed.fishky.flashcard.service.business;

import static org.springframework.web.reactive.function.server.ServerResponse.accepted;
import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;

import com.github.damianszwed.fishky.flashcard.service.adapter.storage.entity.Flashcard;
import com.github.damianszwed.fishky.flashcard.service.adapter.storage.entity.FlashcardFolder;
import com.github.damianszwed.fishky.flashcard.service.configuration.SecurityProperties;
import com.github.damianszwed.fishky.flashcard.service.port.CommandQueryHandler;
import com.github.damianszwed.fishky.flashcard.service.port.IdEncoderDecoder;
import com.github.damianszwed.fishky.flashcard.service.port.OwnerProvider;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolderStorage;
import io.vavr.collection.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuples;

@Slf4j
public class CopyFolderCommandHandler implements CommandQueryHandler {

  private final SecurityProperties securityProperties;
  private final FlashcardFolderStorage flashcardFolderStorage;
  private final IdEncoderDecoder idEncoderDecoder;
  private final OwnerProvider ownerProvider;

  public CopyFolderCommandHandler(
      SecurityProperties securityProperties,
      FlashcardFolderStorage flashcardFolderStorage,
      IdEncoderDecoder idEncoderDecoder,
      OwnerProvider ownerProvider) {
    this.securityProperties = securityProperties;
    this.flashcardFolderStorage = flashcardFolderStorage;
    this.idEncoderDecoder = idEncoderDecoder;
    this.ownerProvider = ownerProvider;
  }

  @Override
  public Mono<ServerResponse> handle(final ServerRequest serverRequest) {
    final String ownerId = switchIfBroughtInOwner(serverRequest.pathVariable("ownerId"),
        securityProperties);
    final String folderIdToCopy = serverRequest.pathVariable("folderId");
    return flashcardFolderStorage.getById(ownerId, folderIdToCopy)
        .flatMap(flashcardFolderToCopy -> retrieveUserId(serverRequest, flashcardFolderToCopy))
        .flatMap(this::retrieveExistingFolder)
        .map(this::mergeFolders)
        .map(this::regenerateIds)
        .flatMap(this::saveCopiedFolder)
        .flatMap(p -> accepted().build())
        .switchIfEmpty(badRequest().build());
  }

  /**
   * If user wants to copy brought in folder, service switches external id with internal id.
   *
   * @param ownerId            String
   * @param securityProperties SecurityProperties
   * @return switchedOwnerId
   */
  private String switchIfBroughtInOwner(String ownerId, SecurityProperties securityProperties) {
    return securityProperties.getSystemUserExternalId().equals(ownerId) ? securityProperties
        .getSystemUserLowerCasedInternalId() : ownerId;
  }

  private Mono<Tuple2<String, FlashcardFolder>> retrieveUserId(ServerRequest serverRequest,
      FlashcardFolder flashcardFolderToCopy) {
    log.info("On copying {}'s {} - retrieve userId operation.", flashcardFolderToCopy.getOwner(),
        flashcardFolderToCopy.getName());
    return Mono.justOrEmpty(ownerProvider.provide(serverRequest))
        .map(userId -> Tuples.of(userId, flashcardFolderToCopy));
  }

  private Mono<Tuple3<String, FlashcardFolder, FlashcardFolder>> retrieveExistingFolder(
      Tuple2<String, FlashcardFolder> tuples) {
    log.info("On copying {}'s {} - trying to retrieve existing folder for user {}.",
        tuples.getT2().getOwner(), tuples.getT2().getName(), tuples.getT1());
    return flashcardFolderStorage
        .get(tuples.getT1(), tuples.getT2().getName())
        .map(existingFolder -> Tuples.of(tuples.getT1(), tuples.getT2(), existingFolder))
        .defaultIfEmpty(
            Tuples.of(tuples.getT1(), tuples.getT2(), FlashcardFolder.builder().build()));
  }

  private Tuple2<String, FlashcardFolder> mergeFolders(
      Tuple3<String, FlashcardFolder, FlashcardFolder> tuples) {
    final FlashcardFolder flashcardFolderToCopy = tuples.getT2();
    final FlashcardFolder existingOrEmptyFlashcardFolder = tuples.getT3();
    final FlashcardFolder mergedFlashcardFolder = flashcardFolderToCopy.toBuilder()
        .flashcards(mergeFlashcards(flashcardFolderToCopy, existingOrEmptyFlashcardFolder))
        .build();
    return Tuples.of(tuples.getT1(), mergedFlashcardFolder);
  }

  private java.util.List<Flashcard> mergeFlashcards(FlashcardFolder flashcardFolderToCopy,
      FlashcardFolder existingOrEmptyFlashcardFolder) {
    return List.ofAll(Stream.concat(
                existingOrEmptyFlashcardFolder.getFlashcards().stream(),
                flashcardFolderToCopy.getFlashcards().stream())
            .collect(Collectors.toList()))
        .distinctBy(Flashcard::getQuestion)
        .toJavaList();
  }

  private Tuple2<String, FlashcardFolder> regenerateIds(Tuple2<String, FlashcardFolder> tuples) {
    log.info("On copying {}'s {} - regenerating ids for user {}.", tuples.getT2().getOwner(),
        tuples.getT2().getName(), tuples.getT1());
    return Tuples.of(tuples.getT1(), tuples.getT2()
        .toBuilder()
        .id(idEncoderDecoder.encodeId(tuples.getT1(), tuples.getT2().getName()))
        .owner(tuples.getT1())
        .flashcards(tuples.getT2().getFlashcards().stream().map(
            flashcard -> flashcard.toBuilder()
                .id(idEncoderDecoder.encodeId(tuples.getT1(), flashcard.getQuestion()))
                .build()
        ).collect(Collectors.toList()))
        .build());
  }

  private Mono<FlashcardFolder> saveCopiedFolder(Tuple2<String, FlashcardFolder> tuples) {
    return flashcardFolderStorage.save(tuples.getT1(), tuples.getT2());
  }
}
