package com.github.damianszwed.fishky.proxy.business;

import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class HealthCheckHandler implements CommandQueryHandler {

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    return ServerResponse.ok().build();
  }
}
