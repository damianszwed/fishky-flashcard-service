package com.github.damianszwed.fishky.flashcard.service.business;

import static org.springframework.web.reactive.function.server.ServerResponse.accepted;
import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;

import com.github.damianszwed.fishky.flashcard.service.port.CommandQueryHandler;
import com.github.damianszwed.fishky.flashcard.service.port.EventTrigger;
import com.github.damianszwed.fishky.flashcard.service.port.OwnerProvider;
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
    return Mono.justOrEmpty(ownerProvider.provide(serverRequest))
        .doOnNext(getAllFoldersEventTrigger::fireUp)
        .flatMap(p -> accepted().build())
        .switchIfEmpty(badRequest().build());
  }
}
