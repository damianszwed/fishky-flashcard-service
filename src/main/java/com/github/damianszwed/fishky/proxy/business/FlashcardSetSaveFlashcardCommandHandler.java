package com.github.damianszwed.fishky.proxy.business;

import static org.springframework.web.reactive.function.server.ServerResponse.accepted;

import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import com.github.damianszwed.fishky.proxy.port.IdEncoderDecoder;
import com.github.damianszwed.fishky.proxy.port.OwnerProvider;
import com.github.damianszwed.fishky.proxy.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardSetStorage;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class FlashcardSetSaveFlashcardCommandHandler implements CommandQueryHandler {

  private final FlashcardSetStorage flashcardSetStorage;
  private final IdEncoderDecoder idEncoderDecoder;
  private final OwnerProvider ownerProvider;

  public FlashcardSetSaveFlashcardCommandHandler(
      FlashcardSetStorage flashcardSetStorage,
      IdEncoderDecoder idEncoderDecoder,
      OwnerProvider ownerProvider) {
    this.flashcardSetStorage = flashcardSetStorage;
    this.idEncoderDecoder = idEncoderDecoder;
    this.ownerProvider = ownerProvider;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    return serverRequest.bodyToMono(Flashcard.class)
        .doOnNext(flashcardToSave ->
            flashcardSetStorage.getById(serverRequest.pathVariable("id"))
                .subscribe(flashcardSet -> {
                  String id = idEncoderDecoder
                      .encodeId(ownerProvider.provide(serverRequest),
                          flashcardToSave.getQuestion());
                  List<Flashcard> flashcardsWithOneRemoved = flashcardSet.getFlashcards().stream()
                      .filter(flashcard -> !flashcard.getId().equals(id))
                      .collect(Collectors.toList());
                  flashcardsWithOneRemoved.add(flashcardToSave
                      .toBuilder()
                      .id(id)
                      .build());

                  flashcardSetStorage.save(
                      flashcardSet.toBuilder()
                          .flashcards(flashcardsWithOneRemoved)
                          .build()
                  );
                }))
        .flatMap(flashcard -> accepted().build());
  }
}
