package com.github.damianszwed.fishky.proxy.business;

import static org.springframework.web.reactive.function.server.ServerResponse.accepted;

import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import com.github.damianszwed.fishky.proxy.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardSaver;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class SaveCommandHandler implements CommandQueryHandler {

  private FlashcardSaver flashcardSaver;

  public SaveCommandHandler(FlashcardSaver flashcardSaver) {
    this.flashcardSaver = flashcardSaver;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    return serverRequest.bodyToMono(Flashcard.class)
        .doOnNext(flashcard -> flashcardSaver.save(flashcard))
        .flatMap(flashcard -> accepted().build());
  }
}
