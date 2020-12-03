package com.github.damianszwed.fishky.proxy.business;

import static org.springframework.web.reactive.function.server.ServerResponse.accepted;

import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import com.github.damianszwed.fishky.proxy.port.IdEncoderDecoder;
import com.github.damianszwed.fishky.proxy.port.OwnerProvider;
import com.github.damianszwed.fishky.proxy.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardFolderStorage;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class FlashcardFolderModifyFlashcardCommandHandler implements CommandQueryHandler {

  private final FlashcardFolderStorage flashcardFolderStorage;
  private final IdEncoderDecoder idEncoderDecoder;
  private final OwnerProvider ownerProvider;

  public FlashcardFolderModifyFlashcardCommandHandler(
      FlashcardFolderStorage flashcardFolderStorage,
      IdEncoderDecoder idEncoderDecoder,
      OwnerProvider ownerProvider) {
    this.flashcardFolderStorage = flashcardFolderStorage;
    this.idEncoderDecoder = idEncoderDecoder;
    this.ownerProvider = ownerProvider;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    return serverRequest.bodyToMono(Flashcard.class)
        .doOnNext(flashcardToModify ->
            flashcardFolderStorage.getById(serverRequest.pathVariable("id"))
                .subscribe(flashcardFolder -> {
                  String existingFlashcardId = flashcardToModify.getId();
                  List<Flashcard> flashcardsWithOneRemoved = flashcardFolder.getFlashcards()
                      .stream()
                      .filter(flashcard -> !flashcard.getId().equals(existingFlashcardId))
                      .collect(Collectors.toList());
                  flashcardsWithOneRemoved.add(flashcardToModify
                      .toBuilder()
                      .id(existingFlashcardId)
                      .build());

                  flashcardFolderStorage.save(
                      flashcardFolder.toBuilder()
                          .flashcards(flashcardsWithOneRemoved)
                          .build()
                  );
                }))
        .flatMap(flashcard -> accepted().build());
  }
}
