package com.github.damianszwed.fishky.proxy.component.driver;

public interface Student {

  void queriesForFlashcardFolders();

  void receivesFlashcardFolders(String flashcardFolders);

  void savesFlashcardInFolder(String newFlashcard, String flashcardFolderName);

  void deletesFlashcardFromFolder(String existingFlashcardId, String flashcardFolderName);

  void commandsForAllFlashcards();

  void isListeningOnFlashcards();

  void isNotifiedAboutAllFlashcards();
}