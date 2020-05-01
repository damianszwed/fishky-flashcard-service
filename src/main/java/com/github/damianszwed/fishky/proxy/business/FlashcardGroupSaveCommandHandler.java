package com.github.damianszwed.fishky.proxy.business;

import static org.springframework.web.reactive.function.server.ServerResponse.accepted;

import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardGroup;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardGroupStorage;
import java.util.Collections;
import java.util.Optional;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class FlashcardGroupSaveCommandHandler implements CommandQueryHandler {

  private FlashcardGroupStorage flashcardGroupStorage;

  public FlashcardGroupSaveCommandHandler(FlashcardGroupStorage flashcardGroupStorage) {
    this.flashcardGroupStorage = flashcardGroupStorage;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    return serverRequest.bodyToMono(FlashcardGroup.class)
        .doOnNext(flashcardGroup -> flashcardGroupStorage.save(abc(flashcardGroup)))
        .flatMap(flashcard -> accepted().build());
  }

  private FlashcardGroup abc(FlashcardGroup flashcardGroup) {
    return flashcardGroup.toBuilder()
        .id(Optional.ofNullable(flashcardGroup.getId())
            .orElse("user1@example.com-" + flashcardGroup.getName()))
        .owner("user1@example.com")
        .flashcards(
            Optional.ofNullable(flashcardGroup.getFlashcards()).orElse(Collections.emptyList()))
        .build();
  }
}
