package com.github.damianszwed.fishky.flashcard.service.component.driver;

import org.springframework.http.HttpStatus;

public interface Student {

  void queriesForFlashcardFolders();

  void receivesFlashcardFolders(String flashcardFolders);

  void savesFlashcardInFolder(String newFlashcard, String flashcardFolderId);

  void modifiesFlashcardInFolder(String modifiedFlashcard, String flashcardFolderId);

  void deletesFlashcardFromFolder(String existingFlashcardId, String flashcardFolderId);

  void commandsForAllFlashcardFolders();

  void isListeningOnFlashcardFolders();

  void isNotifiedAboutAllFlashcardFolders();

  void createsFolder(String flashcardFolder);

  void receivesAnError(HttpStatus status);
}