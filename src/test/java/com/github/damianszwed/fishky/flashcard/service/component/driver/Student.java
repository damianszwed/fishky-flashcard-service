package com.github.damianszwed.fishky.flashcard.service.component.driver;

import org.springframework.http.HttpStatus;

public interface Student {

  void queriesForFlashcardFolders();

  void queriesForFlashcardFoldersByOwner(String ownerId);

  void receivesFlashcardFolders(String flashcardFolders);

  void savesFlashcardInFolder(String newFlashcard, String flashcardFolderId);

  void modifiesFlashcardInFolder(String modifiedFlashcard, String flashcardFolderId);

  void deletesFlashcardFromFolder(String existingFlashcardId, String flashcardFolderId);

  void commandsForAllFlashcardFolders();

  void isListeningOnFlashcardFolders();

  void isNotifiedAboutAllFlashcardFolders();

  void createsFolder(String flashcardFolder);

  void receives(HttpStatus status);

  void deletesFolder(String folderId);

  void copiesFlashcardFolder(String folderId, String owner);
}