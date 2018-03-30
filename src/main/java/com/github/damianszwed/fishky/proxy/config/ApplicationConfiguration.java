package com.github.damianszwed.fishky.proxy.config;

import com.github.damianszwed.fishky.proxy.adapter.storage.development.FlashcardDevelopmentStorage;
import com.github.damianszwed.fishky.proxy.adapter.storage.production.FlashcardStorage;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardProvider;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardRemover;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardSaver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    FlashcardProvider flashcardProvider() {
        return new FlashcardDevelopmentStorage();
    }

    @Bean
    FlashcardRemover flashcardRemover(FlashcardDevelopmentStorage flashcardStorage) {
        return flashcardStorage;
    }

    @Bean
    FlashcardSaver flashcardSaver(FlashcardDevelopmentStorage flashcardStorage) {
        return flashcardStorage;
    }
}
