package com.github.damianszwed.fishky.proxy.business;

import static org.springframework.web.reactive.function.server.ServerResponse.accepted;

import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import com.github.damianszwed.fishky.proxy.port.IdEncoderDecoder;
import com.github.damianszwed.fishky.proxy.port.OwnerProvider;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardSet;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardSetStorage;
import java.util.Collections;
import java.util.Optional;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class FlashcardSetSaveCommandHandler implements CommandQueryHandler {

  private final FlashcardSetStorage flashcardSetStorage;
  private final IdEncoderDecoder idEncoderDecoder;
  private final OwnerProvider ownerProvider;

  public FlashcardSetSaveCommandHandler(
      FlashcardSetStorage flashcardSetStorage,
      IdEncoderDecoder idEncoderDecoder,
      OwnerProvider ownerProvider) {
    this.flashcardSetStorage = flashcardSetStorage;
    this.idEncoderDecoder = idEncoderDecoder;
    this.ownerProvider = ownerProvider;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    return serverRequest.bodyToMono(FlashcardSet.class)
        .doOnNext(
            flashcardSet -> flashcardSetStorage.save(withIdAndOwner(flashcardSet, serverRequest)))
        .flatMap(flashcard -> accepted().build());
  }

  private FlashcardSet withIdAndOwner(
      FlashcardSet flashcardSet,
      ServerRequest serverRequest) {
    return flashcardSet.toBuilder()
        .id(idEncoderDecoder.encodeId(ownerProvider.provide(serverRequest), flashcardSet.getName()))
        .owner(ownerProvider.provide(serverRequest))
        .flashcards(
            Optional.ofNullable(flashcardSet.getFlashcards()).orElse(Collections.emptyList()))
        .build();
  }
}
