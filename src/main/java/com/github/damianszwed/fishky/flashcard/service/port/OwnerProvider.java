package com.github.damianszwed.fishky.flashcard.service.port;

import org.springframework.web.reactive.function.server.ServerRequest;

public interface OwnerProvider {

  String provide(ServerRequest serverRequest);
}
