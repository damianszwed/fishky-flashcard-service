package com.github.damianszwed.fishky.flashcard.service.adapter.security;

import com.github.damianszwed.fishky.flashcard.service.port.OwnerProvider;
import org.springframework.web.reactive.function.server.ServerRequest;

public class DevelopmentOwnerProvider implements OwnerProvider {

  @Override
  public String provide(ServerRequest serverRequest) {
    return "user1@example.com";
  }
}
