package com.github.damianszwed.fishky.proxy.component.driver;

public interface Student {

  void queriesForFlashcards();

  void receivesFlashcards(String expectedJson);

  void savesFlashcard(String flashcard);

  void commandsForAllFlashcards();

  void isListeningOnFlashcards();

  void isNotifiedAboutAllFlashcards();
}