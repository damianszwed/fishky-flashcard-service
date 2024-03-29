package com.github.damianszwed.fishky.flashcard.service.business;

import static org.springframework.web.reactive.function.server.ServerResponse.permanentRedirect;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

import com.github.damianszwed.fishky.flashcard.service.port.CommandQueryHandler;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


/**
 * TODO(Damian.Szwed) swagger for WebFlux
 */
@Slf4j
public class SwaggerHandler implements CommandQueryHandler {

  @Override
  public Mono<ServerResponse> handle(ServerRequest serverRequest) {
    try {
      return permanentRedirect(new URI("index.html")).build();
    } catch (URISyntaxException e) {
      log.error("An error occurred while creating URI.", e);
      return status(HttpStatus.INTERNAL_SERVER_ERROR)
          .bodyValue("Error while creating swagger page.");
    }
  }
}
