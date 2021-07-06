package com.github.damianszwed.fishky.proxy.component;

import com.github.damianszwed.fishky.proxy.component.driver.FishkyProxyDriver;
import com.github.damianszwed.fishky.proxy.component.driver.SpringTestConfiguration;
import com.github.damianszwed.fishky.proxy.configuration.BusinessConfiguration;
import com.github.damianszwed.fishky.proxy.configuration.CommandQueryWebConfiguration;
import com.github.damianszwed.fishky.proxy.configuration.DevelopmentSpecificConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.reactive.ReactiveOAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
    SpringTestConfiguration.class,
    DevelopmentSpecificConfiguration.class,
    BusinessConfiguration.class,
    CommandQueryWebConfiguration.class
})
@WebFluxTest(excludeAutoConfiguration = {
    ReactiveSecurityAutoConfiguration.class,
    ReactiveUserDetailsServiceAutoConfiguration.class,
    ReactiveOAuth2ResourceServerAutoConfiguration.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("Fishky proxy service")
@ActiveProfiles("development")
class FishkyProxyServiceTest {

  @Autowired
  private FishkyProxyDriver fishkyProxyDriver;

  @Test
  @DisplayName("Service should return all flashcard folders on query.")
  void shouldReturnAllFlashcardFolders() {
    fishkyProxyDriver.with(context -> {
      context.when().student().queriesForFlashcardFolders();
      context.then().student().receivesFlashcardFolders(
          OutputSamples.FLASHCARD_FOLDERS
      );
    });
  }

  @Test
  @DisplayName("Service should save flashcard in particular folder on demand.")
  void shouldSaveFlashcardInParticularFolder() {
    fishkyProxyDriver.with(context -> {
      context.when().student()
          .savesFlashcardInFolder(InputSamples.NEW_FLASHCARD, InputSamples.FLASHCARD_FOLDER_ID);
      context.when().student().queriesForFlashcardFolders();
      context.then().student().receivesFlashcardFolders(
          OutputSamples.FLASHCARD_FOLDERS_WITH_NEW_FLASHCARD
      );
    });
  }

  @Test
  @DisplayName("Service should modify flashcard on demand from particular folder.")
  void shouldModifyFlashcardInParticularFolder() {
    fishkyProxyDriver.with(context -> {
      context.when().student()
          .modifiesFlashcardInFolder(
              InputSamples.MODIFIED_FLASHCARD,
              InputSamples.FLASHCARD_FOLDER_ID);
      context.when().student().queriesForFlashcardFolders();
      context.then().student().receivesFlashcardFolders(
          OutputSamples.FLASHCARD_FOLDERS_WITH_ONE_FLASHCARD_MODIFIED
      );
    });
  }

  @Test
  @DisplayName("Service should delete flashcard on demand from particular folder.")
  void shouldDeleteFlashcardFromParticularFolder() {
    fishkyProxyDriver.with(context -> {
      context.when().student()
          .deletesFlashcardFromFolder(
              InputSamples.EXISTING_FLASHCARD_ID,
              InputSamples.FLASHCARD_FOLDER_ID);
      context.when().student().queriesForFlashcardFolders();
      context.then().student().receivesFlashcardFolders(
          OutputSamples.FLASHCARD_FOLDERS_WITHOUT_ONE_FLASHCARD
      );
    });
  }

  @Test
  @DisplayName("Service should notify about all flashcard folders on demand.")
  void shouldNotifyAboutAllFlashcardFoldersWhenCommandsForAllFlashcardFolders() {
    fishkyProxyDriver.with(context -> {
      context.given().student().isListeningOnFlashcardFolders();
      context.when().student().commandsForAllFlashcardFolders();
      context.then().student().isNotifiedAboutAllFlashcardFolders();
    });
  }
}