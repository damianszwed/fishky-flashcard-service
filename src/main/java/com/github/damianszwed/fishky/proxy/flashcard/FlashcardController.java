package com.github.damianszwed.fishky.proxy.flashcard;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.Arrays.asList;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class FlashcardController {

    @RequestMapping("/flashcards")
    public List<Flashcard> flashcards() {
        return asList(
            Flashcard.builder().id("user1@example.com-questiona").question("questionA").answer("answerA").build(),
            Flashcard.builder().id("user1@example.com-questionb").question("questionB").answer("answerB").build(),
            Flashcard.builder().id("user1@example.com-questionc").question("questionC").answer("answerC").build()
        );
    }
}
