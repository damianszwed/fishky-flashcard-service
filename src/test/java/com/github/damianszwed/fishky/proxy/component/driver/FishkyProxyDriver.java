package com.github.damianszwed.fishky.proxy.component.driver;

import static org.junit.jupiter.api.Assertions.fail;

import com.github.damianszwed.fishky.proxy.port.flashcard.EventSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import reactor.test.StepVerifier;
import reactor.test.StepVerifier.Step;

public class FishkyProxyDriver {

  private final WebTestClient webTestClient;
  private final EventSource eventSource;
  private final Student student;

  FishkyProxyDriver(WebTestClient webTestClient,
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

    private ResponseSpec response;
    private Step flashcardStep;

    @Override
    public void queriesForFlashcardFolders() {
      response = webTestClient
          .get().uri("/flashcardFolders")
          .accept(MediaType.APPLICATION_JSON)
          .exchange();
    }

    @Override
    public void receivesFlashcardFolders(String flashcardFolders) {
      response.expectBody().json(flashcardFolders);
    }

    @Override
    public void savesFlashcardInFolder(String newFlashcard, String flashcardFolderId) {
      webTestClient
          .post()
          .uri("/flashcardFolders/{flashcardFolderId}/flashcards", flashcardFolderId)
          .accept(MediaType.APPLICATION_JSON)
          .contentType(MediaType.APPLICATION_JSON)
          .bodyValue(newFlashcard)
          .exchange().expectStatus().isAccepted();
    }

    @Override
    public void deletesFlashcardFromFolder(String flashcardId, String flashcardFolderId) {
      webTestClient
          .delete()
          .uri("/flashcardFolders/{flashcardFolderId}/flashcards/{flashcardId}",
              flashcardFolderId,
              flashcardId)
          .accept(MediaType.APPLICATION_JSON)
          .exchange().expectStatus().isAccepted();
    }

    @Override
    public void isListeningOnFlashcards() {
      flashcardStep = StepVerifier.create(eventSource.getFlux()).expectNextCount(3);
    }

    @Override
    public void commandsForAllFlashcards() {
      webTestClient
          .get().uri("/getAllFlashcardsCommand")
          .exchange().expectStatus().isAccepted();
    }

    @Override
    public void isNotifiedAboutAllFlashcards() {
      flashcardStep.thenCancel().verify();
    }
  }
}
