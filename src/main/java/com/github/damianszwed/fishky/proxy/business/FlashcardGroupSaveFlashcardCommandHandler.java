package com.github.damianszwed.fishky.proxy.business;

import static org.springframework.web.reactive.function.server.ServerResponse.accepted;

import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import com.github.damianszwed.fishky.proxy.port.IdEncoderDecoder;
import com.github.damianszwed.fishky.proxy.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardGroupStorage;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class FlashcardGroupSaveFlashcardCommandHandler implements CommandQueryHandler {

  private final FlashcardGroupStorage flashcardGroupStorage;
  private final IdEncoderDecoder idEncoderDecoder;

  public FlashcardGroupSaveFlashcardCommandHandler(
      FlashcardGroupStorage flashcardGroupStorage,
      IdEncoderDecoder idEncoderDecoder) {
    this.flashcardGroupStorage = flashcardGroupStorage;
    this.idEncoderDecoder = idEncoderDecoder;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    return serverRequest.bodyToMono(Flashcard.class)
        .doOnNext(flashcardToSave ->
            flashcardGroupStorage.getById(serverRequest.pathVariable("id"))
                .subscribe(flashcardGroup -> {
                  String id = idEncoderDecoder
                      .encodeId("user1@example.com", flashcardToSave.getQuestion());
                  List<Flashcard> flashcardsWithOneRemoved = flashcardGroup.getFlashcards().stream()
                      .filter(flashcard -> !flashcard.getId().equals(id))
                      .collect(Collectors.toList());
                  flashcardsWithOneRemoved.add(flashcardToSave
                      .toBuilder()
                      .id(id)
                      .build());

                  flashcardGroupStorage.save(
                      flashcardGroup.toBuilder()
                          .flashcards(flashcardsWithOneRemoved)
                          .build()
                  );
                }))
        .flatMap(flashcard -> accepted().build());
  }
}
