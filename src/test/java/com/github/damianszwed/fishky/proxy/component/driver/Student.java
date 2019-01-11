package com.github.damianszwed.fishky.proxy.component.driver;

public interface Student {

  void queryForFlashcards();

  void receivesFlashcards(String expectedJson);

  void commandsForAllFlashcards();

  void isListeningOnFlashcards();

  void isNotifiedAboutAllFlashcards();
}