package com.github.damianszwed.fishky.proxy.application;

import com.github.damianszwed.fishky.proxy.port.flashcard.Flashcard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@Slf4j
public class ServerSentEvent {

  private FlashcardProviderFlow flashcardProviderFlow;

  public ServerSentEvent(
      @Autowired
          FlashcardProviderFlow flashcardProviderFlow) {
    this.flashcardProviderFlow = flashcardProviderFlow;
  }

  @GetMapping(value = "/notifyOnFlashcards", produces =
      MediaType.APPLICATION_STREAM_JSON_VALUE)
  public Flux<Flashcard> getData() {
    return flashcardProviderFlow.getSomeFLux();
  }
}
