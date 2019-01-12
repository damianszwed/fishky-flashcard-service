package com.github.damianszwed.fishky.proxy.business;

import static org.springframework.web.reactive.function.server.ServerResponse.accepted;

import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * Example trigger how to command to send all flashcards.
 */
public class GetAllCommandHandler implements CommandQueryHandler {

  private FlashcardProviderFlow flashcardProviderFlow;

  public GetAllCommandHandler(FlashcardProviderFlow flashcardProviderFlow) {

    this.flashcardProviderFlow = flashcardProviderFlow;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    flashcardProviderFlow.getAll();
    return accepted().build();
  }
}
