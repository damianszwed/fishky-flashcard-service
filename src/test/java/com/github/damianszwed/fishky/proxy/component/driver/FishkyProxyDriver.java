package com.github.damianszwed.fishky.proxy.component.driver;

import static org.junit.jupiter.api.Assertions.fail;

import com.github.damianszwed.fishky.proxy.port.flashcard.EventSource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

public class FishkyProxyDriver {

  private final WebTestClient webTestClient;
  private final EventSource eventSource;
  private final Student student;
  private ResponseSpec response;

  public FishkyProxyDriver(WebTestClient webTestClient,
      EventSource eventSource) {
    this.webTestClient = webTestClient;
    this.eventSource = eventSource;
    this.student = createStudent();

  }

  public void with(TestCode test) {
    try {
      test.run(this);
    } catch (Exception e) {
      fail(e);
    }
  }

  public Given when() {
    return () -> student;
  }

  public Given then() {
    return () -> student;
  }

  private Student createStudent() {
    return new StudentImpl();
  }

  public Given given() {
    return () -> student;
  }

  private class StudentImpl implements Student {

    @Override
    public void isListeningOnFlashcards() {
      //      flashcardFluxExchangeResult = webTestClient.get()
      //          .uri("/flashcardsEventStream")
      //          .accept(MediaType.TEXT_EVENT_STREAM).exchange().expectStatus().isOk()
      //          .returnResult(Flashcard.class);
    }

    @Override
    public void commandsForAllFlashcards() {
      webTestClient
          .get().uri("/flashcards")
          .exchange().expectStatus().isAccepted();
    }

    @Override
    public void receivesAllFlashcards() {
      //      response.expectHeader().contentType(MediaType.APPLICATION_OCTET_STREAM)
      //          .expectBodyList(Flashcard.class).hasSize(3);
    }
  }
}
