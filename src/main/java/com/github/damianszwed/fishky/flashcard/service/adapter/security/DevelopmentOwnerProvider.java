package com.github.damianszwed.fishky.flashcard.service.adapter.security;

import com.github.damianszwed.fishky.flashcard.service.port.OwnerProvider;
import java.util.Optional;
import org.springframework.web.reactive.function.server.ServerRequest;

public class DevelopmentOwnerProvider implements OwnerProvider {

  @Override
  public Optional<String> provide(ServerRequest serverRequest) {
    return Optional.of("user1@example.com");
  }
}
