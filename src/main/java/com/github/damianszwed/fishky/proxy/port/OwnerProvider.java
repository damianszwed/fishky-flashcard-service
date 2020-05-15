package com.github.damianszwed.fishky.proxy.port;

import org.springframework.web.reactive.function.server.ServerRequest;

public interface OwnerProvider {

  String provide(ServerRequest serverRequest);
}
