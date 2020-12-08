package com.github.damianszwed.fishky.proxy.component.driver;

public interface Student {

  void queriesForFlashcardFolders();

  void receivesFlashcardFolders(String flashcardFolders);

  void savesFlashcardInFolder(String newFlashcard, String flashcardFolderId);

  void modifiesFlashcardInFolder(String modifiedFlashcard, String flashcardFolderId);

  void deletesFlashcardFromFolder(String existingFlashcardId, String flashcardFolderId);

  void commandsForAllFlashcardFolders();

  @Deprecated
  void commandsForAllFlashcards();

  void isListeningOnFlashcardFolders();

  @Deprecated
  void isListeningOnFlashcards();

  void isNotifiedAboutAllFlashcardFolders();

  @Deprecated
  void isNotifiedAboutAllFlashcards();
}