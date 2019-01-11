package com.github.damianszwed.fishky.proxy.component;

import com.github.damianszwed.fishky.proxy.component.driver.FishkyProxyDriver;
import com.github.damianszwed.fishky.proxy.component.driver.SpringTestConfiguration;
import com.github.damianszwed.fishky.proxy.configuration.ApplicationConfiguration;
import com.github.damianszwed.fishky.proxy.configuration.CommandWebConfiguration;
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
    ApplicationConfiguration.class,
    CommandWebConfiguration.class
})
@WebFluxTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("Fishky proxy service")
class FishkyProxyServiceTest {

  @Autowired
  private FishkyProxyDriver fishkyProxyDriver;

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
