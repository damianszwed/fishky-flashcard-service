package com.github.damianszwed.fishky.flashcard.service.business;

import static org.springframework.web.reactive.function.server.ServerResponse.accepted;

import com.github.damianszwed.fishky.flashcard.service.port.CommandQueryHandler;
import com.github.damianszwed.fishky.flashcard.service.port.IdEncoderDecoder;
import com.github.damianszwed.fishky.flashcard.service.port.OwnerProvider;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.flashcard.service.port.flashcard.FlashcardFolderService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
public class FlashcardFolderModifyFlashcardCommandHandler implements CommandQueryHandler {

  private final FlashcardFolderService flashcardFolderService;
  private final IdEncoderDecoder idEncoderDecoder;
  private final OwnerProvider ownerProvider;

  public FlashcardFolderModifyFlashcardCommandHandler(
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
        .doOnNext(flashcardToModify ->
            flashcardFolderService
                .getById(ownerProvider.provide(serverRequest), serverRequest.pathVariable("id"))
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

                  flashcardFolderService.save(
                      ownerProvider.provide(serverRequest), flashcardFolder.toBuilder()
                          .flashcards(flashcardsWithOneRemoved)
                          .build())
                      .subscribe(newFlashcardFolder ->
                          log.info("FlashcardFolder {} has been saved.",
                              newFlashcardFolder.getId()));
                }))
        .flatMap(flashcard -> accepted().build());
  }
}
