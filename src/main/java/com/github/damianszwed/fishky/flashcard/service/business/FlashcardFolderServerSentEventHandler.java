package com.github.damianszwed.fishky.flashcard.service.business;

import com.github.damianszwed.fishky.flashcard.service.port.CommandQueryHandler;
import com.github.damianszwed.fishky.flashcard.service.port.OwnerProvider;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.EventSource;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolder;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FlashcardFolderServerSentEventHandler implements CommandQueryHandler {

  private final EventSource<FlashcardFolder> flashcardFoldersEventSource;
  private final OwnerProvider ownerProvider;

  public FlashcardFolderServerSentEventHandler(
      EventSource<FlashcardFolder> flashcardFoldersEventSource,
      OwnerProvider ownerProvider) {
    this.flashcardFoldersEventSource = flashcardFoldersEventSource;
    this.ownerProvider = ownerProvider;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    return ServerResponse.ok()
        .contentType(MediaType.TEXT_EVENT_STREAM)
        .body(flashcardFoldersEventSource.getFlux(ownerProvider.provide(serverRequest)),
            Flux.class);
  }

}
