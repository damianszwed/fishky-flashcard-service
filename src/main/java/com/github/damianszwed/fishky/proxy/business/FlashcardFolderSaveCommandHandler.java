package com.github.damianszwed.fishky.proxy.business;

import static org.springframework.web.reactive.function.server.ServerResponse.accepted;

import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import com.github.damianszwed.fishky.proxy.port.IdEncoderDecoder;
import com.github.damianszwed.fishky.proxy.port.OwnerProvider;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardFolder;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardFolderStorage;
import java.util.Collections;
import java.util.Optional;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class FlashcardFolderSaveCommandHandler implements CommandQueryHandler {

  private final FlashcardFolderStorage flashcardFolderStorage;
  private final IdEncoderDecoder idEncoderDecoder;
  private final OwnerProvider ownerProvider;

  public FlashcardFolderSaveCommandHandler(
      FlashcardFolderStorage flashcardFolderStorage,
      IdEncoderDecoder idEncoderDecoder,
      OwnerProvider ownerProvider) {
    this.flashcardFolderStorage = flashcardFolderStorage;
    this.idEncoderDecoder = idEncoderDecoder;
    this.ownerProvider = ownerProvider;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    return serverRequest.bodyToMono(FlashcardFolder.class)
        .doOnNext(
            flashcardFolder -> flashcardFolderStorage
                .save(withIdAndOwner(flashcardFolder, serverRequest)))
        .flatMap(flashcard -> accepted().build());
  }

  private FlashcardFolder withIdAndOwner(
      FlashcardFolder flashcardFolder,
      ServerRequest serverRequest) {
    return flashcardFolder.toBuilder()
        .id(idEncoderDecoder
            .encodeId(ownerProvider.provide(serverRequest), flashcardFolder.getName()))
        .owner(ownerProvider.provide(serverRequest))
        .flashcards(
            Optional.ofNullable(flashcardFolder.getFlashcards()).orElse(Collections.emptyList()))
        .build();
  }
}
