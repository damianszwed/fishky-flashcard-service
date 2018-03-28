package com.github.damianszwed.fishky.proxy.integration;

import com.github.damianszwed.fishky.proxy.adapter.storage.development.FlashcardDevelopmentStorage;
import com.github.damianszwed.fishky.proxy.flashcard.FlashcardController;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardProvider;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardRemover;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardSaver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(FlashcardController.class)
public class FlashcardSpecification {

    @TestConfiguration
    static class AskerTestConfiguration {
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

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void removingOneFlashcard() throws Exception {
        this.mockMvc.perform(delete("/flashcard/{id}", "user1@example.com-questionb")).andExpect(status().isOk());

        this.mockMvc.perform(get("/flashcards")).andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value("user1@example.com-questiona"))
            .andExpect(jsonPath("$[0].question").value("questionA"))
            .andExpect(jsonPath("$[0].answer").value("answerA"))
            .andExpect(jsonPath("$[1].id").value("user1@example.com-questionc"))
            .andExpect(jsonPath("$[1].question").value("questionC"))
            .andExpect(jsonPath("$[1].answer").value("answerC"));
    }
}
