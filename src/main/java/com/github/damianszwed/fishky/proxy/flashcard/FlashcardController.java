package com.github.damianszwed.fishky.proxy.flashcard;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.Arrays.asList;

@RestController
public class FlashcardController {

    @RequestMapping("/flashcard")
    public List<Flashcard> flashcards() {
        return asList(
            Flashcard.builder().question("questionA").answer("answerA").build(),
            Flashcard.builder().question("questionB").answer("answerB").build(),
            Flashcard.builder().question("questionC").answer("answerC").build()
        );
    }
}
