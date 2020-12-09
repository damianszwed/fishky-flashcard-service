package com.github.damianszwed.fishky.proxy.business;

import static org.springframework.web.reactive.function.server.ServerResponse.accepted;

import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import com.github.damianszwed.fishky.proxy.port.EventTrigger;
import com.github.damianszwed.fishky.proxy.port.OwnerProvider;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class FlashcardFolderGetAllCommandHandler implements CommandQueryHandler {

  private final EventTrigger getAllFoldersEventTrigger;
  private final OwnerProvider ownerProvider;

  public FlashcardFolderGetAllCommandHandler(
      EventTrigger getAllFoldersEventTrigger,
      OwnerProvider ownerProvider) {
    this.getAllFoldersEventTrigger = getAllFoldersEventTrigger;
    this.ownerProvider = ownerProvider;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    getAllFoldersEventTrigger.fireUp(ownerProvider.provide(serverRequest));
    return accepted().build();
  }
}
