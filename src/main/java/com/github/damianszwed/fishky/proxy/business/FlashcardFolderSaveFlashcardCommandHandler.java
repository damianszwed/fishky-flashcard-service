package com.github.damianszwed.fishky.proxy.business;

import static org.springframework.web.reactive.function.server.ServerResponse.accepted;

import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import com.github.damianszwed.fishky.proxy.port.IdEncoderDecoder;
import com.github.damianszwed.fishky.proxy.port.OwnerProvider;
import com.github.damianszwed.fishky.proxy.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardFolderService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class FlashcardFolderSaveFlashcardCommandHandler implements CommandQueryHandler {

  private final FlashcardFolderService flashcardFolderService;
  private final IdEncoderDecoder idEncoderDecoder;
  private final OwnerProvider ownerProvider;

  public FlashcardFolderSaveFlashcardCommandHandler(
      FlashcardFolderService flashcardFolderService,
      IdEncoderDecoder idEncoderDecoder,
      OwnerProvider ownerProvider) {
    this.flashcardFolderService = flashcardFolderService;
    this.idEncoderDecoder = idEncoderDecoder;
    this.ownerProvider = ownerProvider;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    return serverRequest.bodyToMono(Flashcard.class)
        .doOnNext(flashcardToSave ->
            flashcardFolderService
                .getById(ownerProvider.provide(serverRequest), serverRequest.pathVariable("id"))
                .subscribe(flashcardFolder -> {
                  String id = idEncoderDecoder
                      .encodeId(ownerProvider.provide(serverRequest),
                          flashcardToSave.getQuestion());
                  List<Flashcard> flashcardsWithOneRemoved = flashcardFolder.getFlashcards()
                      .stream()
                      .filter(flashcard -> !flashcard.getId().equals(id))
                      .collect(Collectors.toList());
                  flashcardsWithOneRemoved.add(flashcardToSave
                      .toBuilder()
                      .id(id)
                      .build());

                  flashcardFolderService.save(
                      ownerProvider.provide(serverRequest), flashcardFolder.toBuilder()
                          .flashcards(flashcardsWithOneRemoved)
                          .build()
                  );
                }))
        .flatMap(flashcard -> accepted().build());
  }
}
