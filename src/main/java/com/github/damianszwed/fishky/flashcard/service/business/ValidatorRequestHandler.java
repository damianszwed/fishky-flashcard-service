package com.github.damianszwed.fishky.flashcard.service.business;

import java.util.function.Function;
import javax.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class ValidatorRequestHandler {

  private final Validator validator;

  public ValidatorRequestHandler(Validator validator) {
    this.validator = validator;
  }

  public <T> Mono<ServerResponse> requireValidBody(
      Function<Mono<T>, Mono<ServerResponse>> block,
      ServerRequest request, Class<T> bodyClass) {

    return request
        .bodyToMono(bodyClass)
        .flatMap(
            body -> validator.validate(body).isEmpty()
                ? block.apply(Mono.just(body))
                : ServerResponse.badRequest().build()
        );
  }
}