package com.github.damianszwed.fishky.proxy.application;

import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import com.github.damianszwed.fishky.proxy.port.flashcard.EventSource;
import com.github.damianszwed.fishky.proxy.port.flashcard.Flashcard;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class EventHandler implements CommandQueryHandler {

  private EventSource eventSource;

  public EventHandler(EventSource eventSource) {
    this.eventSource = eventSource;
  }

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_STREAM_JSON)
        .body(eventSource.getFlux(), Flashcard.class);
  }
}
