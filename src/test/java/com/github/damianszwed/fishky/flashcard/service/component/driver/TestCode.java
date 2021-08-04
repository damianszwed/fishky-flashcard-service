package com.github.damianszwed.fishky.flashcard.service.component.driver;

@FunctionalInterface
public interface TestCode {

  void run(FishkyFlashcardServiceDriver driver) throws Exception;
}
