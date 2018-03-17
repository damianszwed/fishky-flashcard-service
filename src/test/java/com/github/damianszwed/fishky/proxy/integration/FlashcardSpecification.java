package com.github.damianszwed.fishky.proxy.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FlashcardSpecification {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void removingOneFlashcard() throws Exception {
        this.mockMvc.perform(delete("/flashcard/user1@example.com-questionb")).andExpect(status().isOk());

        this.mockMvc.perform(get("/flashcards")).andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value("user1@example.com-questiona"))
            .andExpect(jsonPath("$[0].question").value("questionA"))
            .andExpect(jsonPath("$[0].answer").value("answerA"))
            .andExpect(jsonPath("$[1].id").value("user1@example.com-questionc"))
            .andExpect(jsonPath("$[1].question").value("questionC"))
            .andExpect(jsonPath("$[1].answer").value("answerC"));
    }
}
