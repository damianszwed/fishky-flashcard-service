package com.github.damianszwed.fishky.proxy.component;

import com.github.damianszwed.fishky.proxy.component.driver.FishkyProxyDriver;
import com.github.damianszwed.fishky.proxy.component.driver.SpringTestConfiguration;
import com.github.damianszwed.fishky.proxy.configuration.BusinessConfiguration;
import com.github.damianszwed.fishky.proxy.configuration.CommandQueryWebConfiguration;
import com.github.damianszwed.fishky.proxy.configuration.DevelopmentStorageConfiguration;
import com.github.damianszwed.fishky.proxy.configuration.StorageConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
    SpringTestConfiguration.class,
    DevelopmentStorageConfiguration.class,
    StorageConfiguration.class,
    BusinessConfiguration.class,
    CommandQueryWebConfiguration.class
})
@WebFluxTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("Fishky proxy service")
class FishkyProxyServiceTest {

  @Autowired
  private FishkyProxyDriver fishkyProxyDriver;

  @Test
  @DisplayName("Service should return all flashcards on query.")
  void shouldReturnAllFlashcards() {
    fishkyProxyDriver.with(context -> {
      context.when().student().queriesForFlashcards();
      context.then().student().receivesFlashcards(
          OutputSamples.DEFAULT_FLASHCARDS_ARRAY
      );
    });
  }

  @Test
  @DisplayName("Service should save flashcards on demand.")
  void shouldSaveFlashcard() {
    fishkyProxyDriver.with(context -> {
      context.when().student().savesFlashcard(InputSamples.NEW_FLASHCARD);
      context.when().student().queriesForFlashcards();
      context.then().student().receivesFlashcards(
          OutputSamples.FLASHCARDS_ARRAY_WITH_NEW_FLASHCARD
      );
    });
  }

  @Test
  @DisplayName("Service should delete flashcards on demand.")
  void shouldDeleteFlashcard() {
    fishkyProxyDriver.with(context -> {
      context.when().student().deletesFlashcard(InputSamples.EXISTING_FLASHCARD_ID);
      context.when().student().queriesForFlashcards();
      context.then().student().receivesFlashcards(
          OutputSamples.FLASHCARDS_ARRAY_WITHOUT_ONE_FLASHCARD
      );
    });
  }

  @Test
  @DisplayName("Service should notify about all flashcards on demand.")
  void shouldNotifyAboutAllFlashcardsWhenCommandsForAllFlashcards() {
    fishkyProxyDriver.with(context -> {
      context.given().student().isListeningOnFlashcards();
      context.when().student().commandsForAllFlashcards();
      context.then().student().isNotifiedAboutAllFlashcards();
    });
  }
}