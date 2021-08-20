package com.github.damianszwed.fishky.flashcard.service.business;

import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import com.github.damianszwed.fishky.flashcard.service.port.CommandQueryHandler;
import com.github.damianszwed.fishky.flashcard.service.port.OwnerProvider;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolder;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
public class OwnersFoldersGetAllQueryHandler implements CommandQueryHandler {

  private final FlashcardFolderService flashcardFolderService;
  private final OwnerProvider ownerProvider;

  public OwnersFoldersGetAllQueryHandler(
      FlashcardFolderService flashcardFolderService,
      OwnerProvider ownerProvider) {
    this.flashcardFolderService = flashcardFolderService;
    this.ownerProvider = ownerProvider;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    final String ownerId = serverRequest.pathVariable("id");
    //TODO(Damian.Szwed) refactor this
    if (!"broughtin".equals(ownerId)) {
      //TODO(Damian.Szwed) broughtin as static property
      final String ownerIdFromToken = ownerProvider.provide(serverRequest).get();
      if (!ownerIdFromToken.equals(ownerId)) {
        log.error("Security error! User {} tries to get {} flashcards.", ownerIdFromToken, ownerId);
        return badRequest().build();
      }
    }
    return ok().body(
        flashcardFolderService.get(ownerId),
        FlashcardFolder.class);
  }
}
