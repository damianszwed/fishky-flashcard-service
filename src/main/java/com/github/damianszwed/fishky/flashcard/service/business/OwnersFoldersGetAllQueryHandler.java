package com.github.damianszwed.fishky.flashcard.service.business;

import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import com.github.damianszwed.fishky.flashcard.service.adapter.storage.entity.FlashcardFolder;
import com.github.damianszwed.fishky.flashcard.service.adapter.web.resource.FlashcardFolderResource;
import com.github.damianszwed.fishky.flashcard.service.configuration.SecurityProperties;
import com.github.damianszwed.fishky.flashcard.service.port.CommandQueryHandler;
import com.github.damianszwed.fishky.flashcard.service.port.OwnerProvider;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolderStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
public class OwnersFoldersGetAllQueryHandler implements CommandQueryHandler {

  private final SecurityProperties securityProperties;
  private final FlashcardFolderStorage flashcardFolderStorage;
  private final OwnerProvider ownerProvider;

  public OwnersFoldersGetAllQueryHandler(
      SecurityProperties securityProperties,
      FlashcardFolderStorage flashcardFolderStorage,
      OwnerProvider ownerProvider) {
    this.securityProperties = securityProperties;
    this.flashcardFolderStorage = flashcardFolderStorage;
    this.ownerProvider = ownerProvider;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    final String ownerId = serverRequest.pathVariable("ownerId");
    if (securityProperties.getSystemUserExternalId().equals(ownerId)) {
      return getSystemUserFolders();
    }

    return ownerProvider.provide(serverRequest)
        .filter(ownerIdFromToken -> areTheSame(ownerId, ownerIdFromToken))
        .map(flashcardFolderStorage::get)
        .map(flashcardFolderFlux -> flashcardFolderFlux.map(FlashcardFolder::toResource))
        .map(flashcardFolderFlux -> flashcardFolderFlux.map(
            OwnersFoldersGetAllQueryHandler::asOwner))
        .map(flashcardFolderFlux -> ok().body(flashcardFolderFlux, FlashcardFolderResource.class))
        .orElseGet(() -> badRequest().build());
  }

  private Mono<ServerResponse> getSystemUserFolders() {
    return ok().body(
        flashcardFolderStorage.get(
                securityProperties.getSystemUserLowerCasedInternalId())
            .map(FlashcardFolder::toResource)
            .map(flashcardFolderResource -> flashcardFolderResource.toBuilder().isOwner(false)
                .build()),
        FlashcardFolderResource.class);
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

  private static FlashcardFolderResource asOwner(FlashcardFolderResource flashcardFolder) {
    return flashcardFolder.toBuilder().isOwner(true).build();
  }
}
