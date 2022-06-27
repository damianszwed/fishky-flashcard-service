package com.github.damianszwed.fishky.flashcard.service.business;

import static org.springframework.web.reactive.function.server.ServerResponse.accepted;
import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;

import com.github.damianszwed.fishky.flashcard.service.port.CommandQueryHandler;
import com.github.damianszwed.fishky.flashcard.service.port.OwnerProvider;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolder;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Slf4j
public class FlashcardFolderDeleteCommandHandler implements CommandQueryHandler {

  private final FlashcardFolderService flashcardFolderService;
  private final OwnerProvider ownerProvider;

  public FlashcardFolderDeleteCommandHandler(FlashcardFolderService flashcardFolderService,
      OwnerProvider ownerProvider) {
    this.flashcardFolderService = flashcardFolderService;
    this.ownerProvider = ownerProvider;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    return retrieveOwnerIdAndFolderIdToBeRemoved(serverRequest)
        .flatMap(this::retrieveFolderToBeRemoved)
        .filter(this::checkDatabaseFoldersOwnerIdVersusOwnerIdFromRequest)
        .flatMap(this::removeFolder)
        .switchIfEmpty(badRequest().build());
  }

  private Mono<Tuple2<String, String>> retrieveOwnerIdAndFolderIdToBeRemoved(
      ServerRequest serverRequest) {
    return Mono.justOrEmpty(ownerProvider.provide(serverRequest)).flatMap(
        ownerIdFromRequest -> Mono.just(
            Tuples.of(ownerIdFromRequest, serverRequest.pathVariable("id"))));
  }

  private Mono<Tuple2<String, FlashcardFolder>> retrieveFolderToBeRemoved(
      Tuple2<String, String> tuples) {
    log.info("Retrieving {}'s folder {} before security check.", tuples.getT1(), tuples.getT2());
    return flashcardFolderService.getById(tuples.getT1(), tuples.getT2())
        .map(flashcardFolderToBeRemoved -> Tuples.of(tuples.getT1(), flashcardFolderToBeRemoved));
  }

  private boolean checkDatabaseFoldersOwnerIdVersusOwnerIdFromRequest(
      Tuple2<String, FlashcardFolder> tuples) {
    log.info("Security check - database folder's owner id {} vs owner id from request {}.",
        tuples.getT2().getId(), tuples.getT1());
    return tuples.getT2().getOwner().equals(tuples.getT1());
  }

  private Mono<ServerResponse> removeFolder(Tuple2<String, FlashcardFolder> tuples) {
    log.info("Removing folder with id {}.", tuples.getT2().getId());
    return flashcardFolderService.remove(tuples.getT1(), tuples.getT2().getId())
        .then(accepted().build());
  }
}
