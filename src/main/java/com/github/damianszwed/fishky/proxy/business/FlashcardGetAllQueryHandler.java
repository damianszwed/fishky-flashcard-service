package com.github.damianszwed.fishky.proxy.business;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import com.github.damianszwed.fishky.proxy.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardFolder;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardFolderStorage;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class FlashcardGetAllQueryHandler implements CommandQueryHandler {

  private FlashcardFolderStorage flashcardFolderStorage;

  public FlashcardGetAllQueryHandler(FlashcardFolderStorage flashcardFolderStorage) {
    this.flashcardFolderStorage = flashcardFolderStorage;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    return ok().body(
        flashcardFolderStorage.get("user1@example.com", "Default")
            .flatMapIterable(FlashcardFolder::getFlashcards),
        Flashcard.class);
  }
}
