package com.github.damianszwed.fishky.proxy.business;

import static org.springframework.web.reactive.function.server.ServerResponse.accepted;

import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import com.github.damianszwed.fishky.proxy.port.OwnerProvider;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class FlashcardFolderGetAllCommandHandler implements CommandQueryHandler {

  private final FlashcardFolderProviderFlow flashcardFolderProviderFlow;
  private final OwnerProvider ownerProvider;

  public FlashcardFolderGetAllCommandHandler(
      FlashcardFolderProviderFlow flashcardFolderProviderFlow,
      OwnerProvider ownerProvider) {
    this.flashcardFolderProviderFlow = flashcardFolderProviderFlow;
    this.ownerProvider = ownerProvider;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    flashcardFolderProviderFlow.getAll(ownerProvider.provide(serverRequest));
    return accepted().build();
  }
}
