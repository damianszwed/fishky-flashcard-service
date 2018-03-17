package com.github.damianszwed.fishky.proxy.adapter;

import com.github.damianszwed.fishky.proxy.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardProvider;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardRemover;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

@Slf4j
@Scope("singleton")
public class FlashcardStorage implements FlashcardProvider, FlashcardRemover {

    private List<Flashcard> flashcards = new ArrayList<>();

    public FlashcardStorage() {
        flashcards.addAll(
            asList(
                Flashcard.builder().id("user1@example.com-questiona").question("questionA").answer("answerA").build(),
                Flashcard.builder().id("user1@example.com-questionb").question("questionB").answer("answerB").build(),
                Flashcard.builder().id("user1@example.com-questionc").question("questionC").answer("answerC").build()
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
}
