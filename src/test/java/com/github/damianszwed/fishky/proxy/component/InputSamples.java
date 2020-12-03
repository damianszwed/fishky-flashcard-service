package com.github.damianszwed.fishky.proxy.component;

interface InputSamples {

  String NEW_FLASHCARD = "{\n"
      + "  \"question\": \"questionD\",\n"
      + "  \"answer\": \"answerD\"\n"
      + "}";
  String MODIFIED_FLASHCARD = "{\n"
      + "  \"id\": \"dXNlcjFAZXhhbXBsZS5jb20tcXVlc3Rpb25i\",\n"
      + "  \"question\": \"questionDD\",\n"
      + "  \"answer\": \"answerD"
      + "D\"\n"
      + "}";
  String EXISTING_FLASHCARD_ID = "dXNlcjFAZXhhbXBsZS5jb20tcXVlc3Rpb25i";
  String FLASHCARD_FOLDER_ID = "dXNlcjFAZXhhbXBsZS5jb20tZGVmYXVsdA==";
}
