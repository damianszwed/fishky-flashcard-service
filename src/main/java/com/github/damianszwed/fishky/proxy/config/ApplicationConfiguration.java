package com.github.damianszwed.fishky.proxy.config;

import com.github.damianszwed.fishky.proxy.adapter.FlashcardStorage;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    FlashcardProvider flashcardProvider() {
        return new FlashcardStorage();
    }
}
