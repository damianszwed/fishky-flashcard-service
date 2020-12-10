package com.github.damianszwed.fishky.proxy.business;

import static org.springframework.web.reactive.function.server.ServerResponse.accepted;

import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import com.github.damianszwed.fishky.proxy.port.OwnerProvider;
import com.github.damianszwed.fishky.proxy.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardFolder;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardFolderService;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
public class FlashcardFolderDeleteFlashcardCommandHandler implements CommandQueryHandler {

  private final FlashcardFolderService flashcardFolderService;
  private final OwnerProvider ownerProvider;

  public FlashcardFolderDeleteFlashcardCommandHandler(
      FlashcardFolderService flashcardFolderService,
      OwnerProvider ownerProvider) {
    this.flashcardFolderService = flashcardFolderService;
    this.ownerProvider = ownerProvider;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    //TODO(Damian.Szwed) make sure that requester is an owner.
    String flashcardFolderId = serverRequest.pathVariable("flashcardFolderId");
    String flashcardId = serverRequest.pathVariable("flashcardId");
    return Mono.fromSupplier(() -> Void.TYPE)
        .doOnNext((v) -> removeFlashcardFromFlashcardFolder(ownerProvider.provide(serverRequest),
            flashcardFolderId, flashcardId))
        .flatMap(p -> accepted().build());
  }

  private void removeFlashcardFromFlashcardFolder(
      String owner,
      String flashcardFolderId,
      String flashcardId) {
    flashcardFolderService
        .getById(owner, flashcardFolderId)
        .subscribe(flashcardFolder ->
            flashcardFolderService.save(
                owner,
                flashcardFolder.toBuilder()
                    .flashcards(withRemovedParticularFlashcard(flashcardId, flashcardFolder)
                        .collect(Collectors.toList()))
                    .build())
                .subscribe(newFlashcardFolder ->
                    log.info("FlashcardFolder {} has been saved.", newFlashcardFolder.getId())));
  }

  private Stream<Flashcard> withRemovedParticularFlashcard(
      String flashcardId,
      FlashcardFolder flashcardFolder) {
    return flashcardFolder.getFlashcards().stream()
        .filter(flashcard -> !flashcard.getId()
            .equals(flashcardId));
  }
}
