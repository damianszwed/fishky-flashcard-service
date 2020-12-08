package com.github.damianszwed.fishky.proxy.business;

import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import com.github.damianszwed.fishky.proxy.port.OwnerProvider;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardFolder;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class FlashcardFolderServerSentEventHandler implements CommandQueryHandler {

  private final FlashcardFolderProviderFlow flashcardFolderProviderFlow;
  private final OwnerProvider ownerProvider;

  public FlashcardFolderServerSentEventHandler(
      FlashcardFolderProviderFlow flashcardFolderProviderFlow,
      OwnerProvider ownerProvider) {
    this.flashcardFolderProviderFlow = flashcardFolderProviderFlow;
    this.ownerProvider = ownerProvider;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_STREAM_JSON)
        .body(flashcardFolderProviderFlow.getFlux(ownerProvider.provide(serverRequest)),
            FlashcardFolder.class);
  }

}
