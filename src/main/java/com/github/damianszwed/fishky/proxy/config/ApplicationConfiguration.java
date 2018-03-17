package com.github.damianszwed.fishky.proxy.config;

import com.github.damianszwed.fishky.proxy.adapter.FlashcardStorage;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardProvider;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardRemover;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardSaver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    FlashcardProvider flashcardProvider() {
        return new FlashcardStorage();
    }

    @Bean
    FlashcardRemover flashcardRemover(FlashcardStorage flashcardStorage) {
        return flashcardStorage;
    }

    @Bean
    FlashcardSaver flashcardSaver(FlashcardStorage flashcardStorage) {
        return flashcardStorage;
    }
}
