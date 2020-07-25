package com.github.damianszwed.fishky.proxy.business;

import static org.springframework.web.reactive.function.server.ServerResponse.accepted;

import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import com.github.damianszwed.fishky.proxy.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardFolder;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardFolderStorage;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class FlashcardFolderDeleteFlashcardCommandHandler implements CommandQueryHandler {

  private FlashcardFolderStorage flashcardFolderStorage;

  public FlashcardFolderDeleteFlashcardCommandHandler(
      FlashcardFolderStorage flashcardFolderStorage) {
    this.flashcardFolderStorage = flashcardFolderStorage;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    //TODO(Damian.Szwed) make sure that requester is an owner.
    String flashcardFolderId = serverRequest.pathVariable("flashcardFolderId");
    String flashcardId = serverRequest.pathVariable("flashcardId");
    return Mono.fromSupplier(() -> Void.TYPE)
        .doOnNext((v) -> removeFlashcardFromFlashcardFolder(flashcardFolderId, flashcardId))
        .flatMap(p -> accepted().build());
  }

  private void removeFlashcardFromFlashcardFolder(
      String flashcardFolderId,
      String flashcardId) {
    flashcardFolderStorage.getById(flashcardFolderId)
        .subscribe(flashcardFolder ->
            flashcardFolderStorage.save(
                flashcardFolder.toBuilder()
                    .flashcards(withRemovedParticularFlashcard(flashcardId, flashcardFolder)
                        .collect(Collectors.toList()))
                    .build()));
  }

  private Stream<Flashcard> withRemovedParticularFlashcard(
      String flashcardId,
      FlashcardFolder flashcardFolder) {
    return flashcardFolder.getFlashcards().stream()
        .filter(flashcard -> !flashcard.getId()
            .equals(flashcardId));
  }
}
