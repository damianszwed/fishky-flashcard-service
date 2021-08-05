package com.github.damianszwed.fishky.flashcard.service.component;

interface InputSamples {

  //language=JSON
  String NEW_FLASHCARD = "{\n"
      + "  \"question\": \"questionD\",\n"
      + "  \"answers\": [\"answerD\"]\n"
      + "}";
  //language=JSON
  String MODIFIED_FLASHCARD = "{\n"
      + "  \"id\": \"dXNlcjFAZXhhbXBsZS5jb20tcXVlc3Rpb25i\",\n"
      + "  \"question\": \"questionDD\",\n"
      + "  \"answers\": [\"answerDD\"]\n"
      +
      "}";
  String EXISTING_FLASHCARD_ID = "dXNlcjFAZXhhbXBsZS5jb20tcXVlc3Rpb25i";
  String FLASHCARD_FOLDER_ID = "dXNlcjFAZXhhbXBsZS5jb20tZGVmYXVsdA==";
  //language=JSON
  String FLASHCARD_FOLDER_WITH_EMPTY_NAME = "{\n"
      + "  \"name\": \" \",\n"
      + "  \"flashcards\": [],\n"
      + "  \"id\": \"placeholder\"\n"
      + "}";
}
