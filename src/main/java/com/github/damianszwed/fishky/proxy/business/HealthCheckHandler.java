package com.github.damianszwed.fishky.proxy.business;

import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
public class HealthCheckHandler implements CommandQueryHandler {

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    log.info("Healtcheck request has been made.");
    return ServerResponse.ok().build();
  }
}
