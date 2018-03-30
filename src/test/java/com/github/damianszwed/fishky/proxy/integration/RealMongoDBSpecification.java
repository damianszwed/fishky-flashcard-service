package com.github.damianszwed.fishky.proxy.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.github.damianszwed.fishky.proxy.port.flashcard.Flashcard;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Ignore("Only with real mongodb")
public class RealMongoDBSpecification {

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/flashcards")).andReturn();

        ObjectMapper mapper = new ObjectMapper();

        CollectionType javaType = mapper.getTypeFactory()
            .constructCollectionType(List.class, Flashcard.class);
        List<Flashcard> asList = mapper.readValue(result.getResponse().getContentAsString(), javaType);


        asList.forEach(flashcard -> {
            try {
                this.mockMvc.perform(delete("/flashcard/{id}", flashcard.getId()))
                    .andExpect(status().isOk());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        for (int i = 0; i < 3; i++) {
            this.mockMvc.perform(post("/flashcard/")
                .contentType(MediaType.APPLICATION_JSON).content(
                    "{\n" +
                        "  \"question\": \"aQuestion" + i + "\",\n" +
                        "  \"answer\": \"anAnswer" + i + "\"\n" +
                        "}"
                ))
                .andExpect(status().isOk());
        }
    }

    @Test
    public void shouldSavedFlashcards() throws Exception {
        this.mockMvc.perform(get("/flashcards")).andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").exists())
            .andExpect(jsonPath("$[0].question").value("aQuestion0"))
            .andExpect(jsonPath("$[0].answer").value("anAnswer0"))
            .andExpect(jsonPath("$[1].id").exists())
            .andExpect(jsonPath("$[1].question").value("aQuestion1"))
            .andExpect(jsonPath("$[1].answer").value("anAnswer1"))
            .andExpect(jsonPath("$[2].id").exists())
            .andExpect(jsonPath("$[2].question").value("aQuestion2"))
            .andExpect(jsonPath("$[2].answer").value("anAnswer2"))
        ;
    }
}
