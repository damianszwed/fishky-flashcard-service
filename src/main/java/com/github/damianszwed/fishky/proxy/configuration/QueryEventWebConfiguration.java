package com.github.damianszwed.fishky.proxy.configuration;

import java.time.Duration;
import java.util.Random;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@Slf4j
public class QueryEventWebConfiguration {

  @GetMapping(value = "/notifyonEvent", produces =
      MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<String> getData() {
    Random r = new Random();
    int low = 0;
    int high = 50;
    return Flux.fromStream(Stream.generate(() -> r.nextInt(high - low) + low)
        .map(String::valueOf)
        .peek(log::info))
        .map(s -> s)
        .delayElements(Duration.ofSeconds(1));
  }
}
