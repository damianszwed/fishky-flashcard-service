package com.github.damianszwed.fishky.proxy.component.driver;

public interface Student {

  @Deprecated
  void queriesForFlashcards();

  @Deprecated
  void receivesFlashcards(String expectedJson);

  @Deprecated
  void savesFlashcard(String flashcard);

  @Deprecated
  void deletesFlashcard(String existingFlashcard);

  void commandsForAllFlashcards();

  void isListeningOnFlashcards();

  void isNotifiedAboutAllFlashcards();

  void queriesForFlashcardFolders();

  void receivesFlashcardFolders(String flashcardFolders);

  void savesFlashcardInFolder(String newFlashcard, String flashcardFolderName);
}