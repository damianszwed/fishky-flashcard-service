package com.github.damianszwed.fishky.flashcard.service.port;

import java.util.Optional;
import org.springframework.web.reactive.function.server.ServerRequest;

public interface OwnerProvider {

  Optional<String> provide(ServerRequest serverRequest);
}
