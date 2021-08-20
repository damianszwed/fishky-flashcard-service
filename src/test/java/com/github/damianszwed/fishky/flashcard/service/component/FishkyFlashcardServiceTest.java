package com.github.damianszwed.fishky.flashcard.service.component;

import com.github.damianszwed.fishky.flashcard.service.component.driver.FishkyFlashcardServiceDriver;
import com.github.damianszwed.fishky.flashcard.service.component.driver.SpringTestConfiguration;
import com.github.damianszwed.fishky.flashcard.service.configuration.BusinessConfiguration;
import com.github.damianszwed.fishky.flashcard.service.configuration.CommandQueryWebConfiguration;
import com.github.damianszwed.fishky.flashcard.service.configuration.DevelopmentSpecificConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.reactive.ReactiveOAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.HttpStatus;
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
@DisplayName("Fishky flashcard service")
@ActiveProfiles("development")
class FishkyFlashcardServiceTest {

  @Autowired
  private FishkyFlashcardServiceDriver fishkyFlashcardServiceDriver;

  @Test
  @DisplayName("Service should return all flashcard folders on query.")
  void shouldReturnAllFlashcardFolders() {
    fishkyFlashcardServiceDriver.with(context -> {
      context.when().student().queriesForFlashcardFolders();
      context.then().student().receivesFlashcardFolders(
          OutputSamples.FLASHCARD_FOLDERS
      );
    });
  }

  @Test
  @DisplayName("Service should save flashcard in particular folder on demand.")
  void shouldSaveFlashcardInParticularFolder() {
    fishkyFlashcardServiceDriver.with(context -> {
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
    fishkyFlashcardServiceDriver.with(context -> {
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
    fishkyFlashcardServiceDriver.with(context -> {
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
  @DisplayName("Service should allow to delete owned folder.")
  void shouldAllowToDeleteOwnedFolder() {
    fishkyFlashcardServiceDriver.with(context -> {
      context.when().student().deletesFolder(InputSamples.FLASHCARD_FOLDER_ID);
      context.then().student().receives(HttpStatus.ACCEPTED);
    });
  }

  @Test
  @DisplayName("Service should return all brought in flashcard folders on query.")
  void shouldReturnALlBroughtInFlashcardFoldersOnQuery() {
    fishkyFlashcardServiceDriver.with(context -> {
      //TODO(Damian.Szwed) change request
      context.when().student().queriesForFlashcardFolders();
      context.then().student().receivesFlashcardFolders(
          OutputSamples.FLASHCARD_FOLDERS
      );
    });
  }

  @Test
  @DisplayName("Service should delete folder on demand.")
  void shouldDeleteFolderOnDemand() {
    fishkyFlashcardServiceDriver.with(context -> {
      context.when().student().deletesFolder(InputSamples.FLASHCARD_FOLDER_ID);
      context.when().student().queriesForFlashcardFolders();
      context.then().student().receivesFlashcardFolders(
          OutputSamples.EMPTY_FLASHCARD_FOLDERS
      );
    });
  }

  @Test
  @Tag("SSE")
  @DisplayName("Service should notify about all flashcard folders on demand.")
  void shouldNotifyAboutAllFlashcardFoldersWhenCommandsForAllFlashcardFolders() {
    fishkyFlashcardServiceDriver.with(context -> {
      context.given().student().isListeningOnFlashcardFolders();
      context.when().student().commandsForAllFlashcardFolders();
      context.then().student().isNotifiedAboutAllFlashcardFolders();
    });
  }

  @Test
  @Tag("Validation")
  @DisplayName("Service should not allow to create a folder with empty name.")
  void shouldNotAllowToCreateAFolderWithEmptyName() {
    fishkyFlashcardServiceDriver.with(context -> {
      context.when().student().createsFolder(InputSamples.FLASHCARD_FOLDER_WITH_EMPTY_NAME);
      context.then().student().receives(HttpStatus.BAD_REQUEST);
    });
  }
}