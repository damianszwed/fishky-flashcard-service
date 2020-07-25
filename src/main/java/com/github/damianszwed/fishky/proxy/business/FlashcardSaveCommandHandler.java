package com.github.damianszwed.fishky.proxy.business;

import static org.springframework.web.reactive.function.server.ServerResponse.accepted;

import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import com.github.damianszwed.fishky.proxy.port.IdEncoderDecoder;
import com.github.damianszwed.fishky.proxy.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardFolder;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardFolderStorage;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class FlashcardSaveCommandHandler implements CommandQueryHandler {

  private final FlashcardFolderStorage flashcardFolderStorage;
  private final IdEncoderDecoder idEncoderDecoder;

  public FlashcardSaveCommandHandler(
      FlashcardFolderStorage flashcardFolderStorage,
      IdEncoderDecoder idEncoderDecoder) {
    this.flashcardFolderStorage = flashcardFolderStorage;
    this.idEncoderDecoder = idEncoderDecoder;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    return serverRequest.bodyToMono(Flashcard.class)
        .doOnNext(this::saveFlashcardToDefaultFolder)
        .flatMap(flashcard -> accepted().build());
  }

  private void saveFlashcardToDefaultFolder(Flashcard flashcardToSave) {
    flashcardFolderStorage.get("user1@example.com", "Default")
        .switchIfEmpty(createFirstDefaultFlashcardFolder())
        .subscribe(flashcardFolder -> {
          String id = idEncoderDecoder.encodeId("user1@example.com", flashcardToSave.getQuestion());
          List<Flashcard> flashcardsWithOneRemoved = flashcardFolder.getFlashcards().stream()
              .filter(flashcard -> !flashcard.getId().equals(id)).collect(Collectors.toList());
          flashcardsWithOneRemoved.add(flashcardToSave
              .toBuilder()
              .id(id)
              .build());
          flashcardFolderStorage.save(flashcardFolder
              .toBuilder()
              .flashcards(flashcardsWithOneRemoved)
              .build());
        });
  }

  /**
   * Creates first default flashcard folder if user doesn't have any folder.
   *
   * @return first Default flashcard folder
   */
  private Mono<FlashcardFolder> createFirstDefaultFlashcardFolder() {
    return Mono.just(FlashcardFolder.builder()
        .id("dXNlcjFAZXhhbXBsZS5jb20tZGVmYXVsdA==")//user1@example.com-default
        .owner("user1@example.com")
        .name("Default")
        .build());
  }
}
