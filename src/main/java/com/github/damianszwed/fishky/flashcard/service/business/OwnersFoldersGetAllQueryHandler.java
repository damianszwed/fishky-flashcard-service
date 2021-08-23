package com.github.damianszwed.fishky.flashcard.service.business;

import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import com.github.damianszwed.fishky.flashcard.service.configuration.BusinessProperties;
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
    if (BusinessProperties.SYSTEM_USER_ID.equals(ownerId)) {
      return getAllFlashcardsByOwnerId(ownerId);
    }

    return ownerProvider.provide(serverRequest)
        .filter(ownerIdFromToken -> areTheSame(ownerId, ownerIdFromToken))
        .map(ownerIdFromToken -> getAllFlashcardsByOwnerId(ownerId))
        .orElseGet(() -> badRequest().build());
  }

  private static boolean areTheSame(String ownerId, String ownerIdFromToken) {
    if (ownerIdFromToken.equals(ownerId)) {
      return true;
    } else {
      log.error("Security error! User {} tries to get {} flashcards.", ownerIdFromToken,
          ownerId);
      return false;
    }
  }

  private Mono<ServerResponse> getAllFlashcardsByOwnerId(String ownerId) {
    return ok().body(
        flashcardFolderService.get(ownerId),
        FlashcardFolder.class);
  }
}
