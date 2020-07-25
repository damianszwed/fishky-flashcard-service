package com.github.damianszwed.fishky.proxy.business;

import static org.springframework.web.reactive.function.server.ServerResponse.accepted;

import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardFolderStorage;
import java.util.stream.Collectors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class FlashcardDeleteCommandHandler implements CommandQueryHandler {

  private FlashcardFolderStorage flashcardFolderStorage;

  public FlashcardDeleteCommandHandler(FlashcardFolderStorage flashcardFolderStorage) {
    this.flashcardFolderStorage = flashcardFolderStorage;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    return Mono.fromSupplier(() -> Void.TYPE)
        .doOnNext((v) -> removeFlashcardFromDefaultFolder(serverRequest.pathVariable("id")))
        .flatMap(p -> accepted().build());
  }

  private void removeFlashcardFromDefaultFolder(String id) {
    flashcardFolderStorage.get("user1@example.com", "Default")
        .subscribe(flashcardFolder ->
            flashcardFolderStorage.save(flashcardFolder.toBuilder()
                .flashcards(flashcardFolder.getFlashcards().stream()
                    .filter(flashcard -> !flashcard.getId().equals(id))
                    .collect(Collectors.toList()))
                .build()));
  }
}
