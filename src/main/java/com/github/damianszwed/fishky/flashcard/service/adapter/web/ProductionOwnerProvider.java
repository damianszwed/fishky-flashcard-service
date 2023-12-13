package com.github.damianszwed.fishky.flashcard.service.adapter.web;

import com.github.damianszwed.fishky.flashcard.service.port.OwnerProvider;
import java.util.Optional;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.reactive.function.server.ServerRequest;

public class ProductionOwnerProvider implements OwnerProvider {

  private final JwtDecoder jwtDecoder;

  public ProductionOwnerProvider(JwtDecoder jwtDecoder) {
    this.jwtDecoder = jwtDecoder;
  }

  @Override
  public Optional<String> provide(ServerRequest serverRequest) {
    //TODO(Damian.Szwed) error handling.
    //TODO(Damian.Szwed) unit test for splitting the header
    String bearerToken = serverRequest.headers().firstHeader(HttpHeaders.AUTHORIZATION);
    String token = bearerToken.split(" ")[1];
    return Optional.of(jwtDecoder.decode(token).getClaimAsString("sub").toLowerCase());
  }
}
