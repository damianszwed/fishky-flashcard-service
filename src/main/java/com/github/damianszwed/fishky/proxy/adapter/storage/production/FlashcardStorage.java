package com.github.damianszwed.fishky.proxy.adapter.storage.production;

import com.github.damianszwed.fishky.proxy.port.flashcard.Flashcard;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardProvider;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardRemover;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardSaver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.util.List;

@Slf4j
@Scope("singleton")
public class FlashcardStorage implements FlashcardProvider, FlashcardRemover, FlashcardSaver {

    @Autowired
    FlashcardRepository repository;

    @Override
    public List<Flashcard> getFlashcards(String username) {
        return repository.findAll();
    }

    @Override
    public void removeFlashcard(String id) {
        repository.delete(id);
    }

    @Override
    public void saveFlashcard(Flashcard flashcard) {
        repository.save(flashcard);
    }
}
