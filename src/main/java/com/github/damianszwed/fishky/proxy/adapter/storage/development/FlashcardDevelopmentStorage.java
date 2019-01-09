package com.github.damianszwed.fishky.proxy.adapter.storage.development;

import static java.util.Arrays.asList;

import com.github.damianszwed.fishky.proxy.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardProvider;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardRemover;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardSaver;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FlashcardDevelopmentStorage implements FlashcardProvider, FlashcardRemover,
    FlashcardSaver {

  private List<Flashcard> flashcards = new ArrayList<>();

  public FlashcardDevelopmentStorage() {
    flashcards.addAll(
        asList(
            //TODO(damian.szwed) remember about base64 for questions in ids!
            Flashcard.builder().id("user1@example.com-questiona").question("questionA")
                .answer("answerA").build(),
            Flashcard.builder().id("user1@example.com-questionb").question("questionB")
                .answer("answerB").build(),
            Flashcard.builder().id("user1@example.com-questionc").question("questionC")
                .answer("answerC").build()
        )
    );
  }

  @Override
  public List<Flashcard> getFlashcards(String username) {
    return flashcards;
  }

  @Override
  public void removeFlashcard(String id) {
    flashcards.removeIf(givenFlashcard -> givenFlashcard.getId().equals(id));
  }

  @Override
  public void saveFlashcard(Flashcard flashcard) {
    removeFlashcard("user1@example.com-" + flashcard.getQuestion().toLowerCase());
    flashcards.add(Flashcard.builder()
        .answer(flashcard.getAnswer())
        .question(flashcard.getQuestion())
        .id("user1@example.com-" + flashcard.getQuestion().toLowerCase())
        .build());
  }
}