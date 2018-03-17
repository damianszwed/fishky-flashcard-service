package com.github.damianszwed.fishky.proxy.flashcard;

import com.github.damianszwed.fishky.proxy.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardProvider;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardRemover;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class FlashcardController {

    private FlashcardProvider flashcardProvider;
    private FlashcardRemover flashcardRemover;

    @Autowired
    public FlashcardController(FlashcardProvider flashcardProvider, FlashcardRemover flashcardRemover) {
        this.flashcardProvider = flashcardProvider;
        this.flashcardRemover = flashcardRemover;
    }

    @RequestMapping("/flashcards")
    public List<Flashcard> flashcards() {
        return flashcardProvider.getFlashcards("anyUsername");
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "flashcard/{flashcardId:.+}")
    @ResponseStatus(value = HttpStatus.OK)
    public void removeFlashcard(@PathVariable String flashcardId) {
        flashcardRemover.removeFlashcard(flashcardId);
    }

}
