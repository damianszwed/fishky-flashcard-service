package com.github.damianszwed.fishky.proxy.adapter.security;

import com.github.damianszwed.fishky.proxy.port.OwnerProvider;
import org.springframework.web.reactive.function.server.ServerRequest;

public class DevelopmentOwnerProvider implements OwnerProvider {

  @Override
  public String provide(ServerRequest serverRequest) {
    return "user1@example.com";
  }
}
