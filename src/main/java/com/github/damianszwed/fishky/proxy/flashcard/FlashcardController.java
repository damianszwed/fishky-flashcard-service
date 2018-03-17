package com.github.damianszwed.fishky.proxy.flashcard;

import com.github.damianszwed.fishky.proxy.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class FlashcardController {

    private FlashcardProvider flashcardProvider;

    @Autowired
    public FlashcardController(FlashcardProvider flashcardProvider) {
        this.flashcardProvider = flashcardProvider;
    }

    @RequestMapping("/flashcards")
    public List<Flashcard> flashcards() {
        return flashcardProvider.getFlashcards("anyUsername");
    }
}
