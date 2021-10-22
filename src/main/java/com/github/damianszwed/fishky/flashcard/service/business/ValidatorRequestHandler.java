package com.github.damianszwed.fishky.flashcard.service.business;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
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
            body -> {
              final Set<ConstraintViolation<T>> constraintViolations = validator.validate(body);
              return constraintViolations.isEmpty()
                  ? block.apply(Mono.just(body))
                  : badRequest(constraintViolations);
            }
        )
        .doOnError(throwable -> log.warn("An error occurred.", throwable));
  }

  private <T> Mono<? extends ServerResponse> badRequest(
      Set<ConstraintViolation<T>> constraintViolations) {
    final Optional<ConstraintViolation<T>> firstOptionalConstraintViolation = constraintViolations
        .stream().findFirst();
    firstOptionalConstraintViolation.ifPresent(firstConstraintViolation ->
        log.warn("Return badRequest with constraints size {} and first constraint: {}, {}",
            constraintViolations.size(),
            firstConstraintViolation.getPropertyPath(),
            firstConstraintViolation.getMessage())
    );
    return ServerResponse.badRequest().build();
  }
}