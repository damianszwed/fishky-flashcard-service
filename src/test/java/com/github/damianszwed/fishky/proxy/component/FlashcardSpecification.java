package com.github.damianszwed.fishky.proxy.component;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

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
    public void getAllFlashcardsShouldReturnMockedFlashcard() throws Exception {

        this.mockMvc.perform(get("/flashcard")).andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$[0].question").value("questionA"))
            .andExpect(jsonPath("$[0].answer").value("answerA"))
            .andExpect(jsonPath("$[1].question").value("questionB"))
            .andExpect(jsonPath("$[1].answer").value("answerB"))
            .andExpect(jsonPath("$[2].question").value("questionC"))
            .andExpect(jsonPath("$[2].answer").value("answerC"))
        ;
    }
}
