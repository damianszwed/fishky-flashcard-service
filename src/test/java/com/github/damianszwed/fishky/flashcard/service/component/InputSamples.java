package com.github.damianszwed.fishky.flashcard.service.component;

interface InputSamples {

  //language=JSON
  String NEW_FLASHCARD = """
      {
        "question": "questionD",
        "answers": ["answerD"]
      }""";
  //language=JSON
  String MODIFIED_FLASHCARD = """
      {
        "id": "dXNlcjFAZXhhbXBsZS5jb20tcXVlc3Rpb25i",
        "question": "questionDD",
        "answers": ["answerDD"]
      }""";
  //user1@example.com-questionb
  String EXISTING_FLASHCARD_ID = "dXNlcjFAZXhhbXBsZS5jb20tcXVlc3Rpb25i";
  //user1@example.com-default
  String FLASHCARD_USER_1_FIRST_FOLDER_ID = "dXNlcjFAZXhhbXBsZS5jb20tZGVmYXVsdA==";
  //user2@example.com-folder a
  String FLASHCARD_USER_2_FIRST_FOLDER_ID = "dXNlcjJAZXhhbXBsZS5jb20tZm9sZGVyIGE=";
  String SYSTEM_USER_FOLDER_ID = "YnJvdWdodGluLXR1cmlzbQ==";
  String FLASHCARD_TURISM_FOLDER_ID = "dXNlcjFAZXhhbXBsZS5jb20tdHVyaXNt";
  //language=JSON
  String FLASHCARD_FOLDER_WITH_EMPTY_NAME = """
      {
        "name": " ",
        "flashcards": [],
        "id": "placeholder"
      }""";

  //language=JSON
  String TURISM_FLASHCARD_FOLDER = """
      {
        "name": "Turism",
        "flashcards": [{"question":  "questionD", "answers":  ["answerD"]}],
        "id": "placeholder"
      }""";

  //language=JSON
  String DEFAULT_FLASHCARD_FOLDER = """
      {
        "name": "Default",
        "flashcards": []
      }""";
}
