package com.github.damianszwed.fishky.proxy.application;

import static org.springframework.web.reactive.function.server.ServerResponse.accepted;

import com.github.damianszwed.fishky.proxy.port.CommandHandler;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class GetAllCommand implements CommandHandler {

  private FlashcardProviderFlow flashcardProviderFlow;

  public GetAllCommand(FlashcardProviderFlow flashcardProviderFlow) {

    this.flashcardProviderFlow = flashcardProviderFlow;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    flashcardProviderFlow.getAll();
    return accepted().build();
  }
}
