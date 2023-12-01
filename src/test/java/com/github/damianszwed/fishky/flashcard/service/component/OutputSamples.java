package com.github.damianszwed.fishky.flashcard.service.component;

interface OutputSamples {

  //language=JSON
  String FLASHCARD_FOLDERS =
      """
          [
            {
              "id": "dXNlcjFAZXhhbXBsZS5jb20tZGVmYXVsdA==",
              "name": "Default",
              "owner": "user1@example.com",
              "flashcards": [
                {
                  "id": "dXNlcjFAZXhhbXBsZS5jb20tcXVlc3Rpb25h",
                  "question": "questionA",
                  "answers": ["answerA"]
                },
                {
                  "id": "dXNlcjFAZXhhbXBsZS5jb20tcXVlc3Rpb25i",
                  "question": "questionB",
                  "answers": ["answerB"]
                },
                {
                  "id": "dXNlcjFAZXhhbXBsZS5jb20tcXVlc3Rpb25j",
                  "question": "questionC",
                  "answers": ["answerC"]
                }
              ],
              "isOwner": true
              
            }
          ]
          """;

  //language=JSON
  String FLASHCARD_FOLDERS_WITH_NEW_FLASHCARD =
      """
          [
            {
              "id": "dXNlcjFAZXhhbXBsZS5jb20tZGVmYXVsdA==",
              "name": "Default",
              "owner": "user1@example.com",
              "flashcards": [
                {
                  "id": "dXNlcjFAZXhhbXBsZS5jb20tcXVlc3Rpb25h",
                  "question": "questionA",
                  "answers": ["answerA"]
                },
                {
                  "id": "dXNlcjFAZXhhbXBsZS5jb20tcXVlc3Rpb25i",
                  "question": "questionB",
                  "answers": ["answerB"]
                },
                {
                  "id": "dXNlcjFAZXhhbXBsZS5jb20tcXVlc3Rpb25j",
                  "question": "questionC",
                  "answers": ["answerC"]
                },
                {
                  "id": "dXNlcjFAZXhhbXBsZS5jb20tcXVlc3Rpb25k",
                  "question": "questionD",
                  "answers": ["answerD"]
                }
              ],
              "isOwner": true
            }
          ]
          """;

  //language=JSON
  String EMPTY_FLASHCARD_FOLDERS = "[]";

  //language=JSON
  String FLASHCARD_FOLDERS_WITHOUT_ONE_FLASHCARD =
      """
          [
            {
              "id": "dXNlcjFAZXhhbXBsZS5jb20tZGVmYXVsdA==",
              "name": "Default",
              "owner": "user1@example.com",
              "flashcards": [
                {
                  "id": "dXNlcjFAZXhhbXBsZS5jb20tcXVlc3Rpb25h",
                  "question": "questionA",
                  "answers": ["answerA"]
                },
                {
                  "id": "dXNlcjFAZXhhbXBsZS5jb20tcXVlc3Rpb25j",
                  "question": "questionC",
                  "answers": ["answerC"]
                }
              ],
              "isOwner": true
            }
          ]
          """;

  //language=JSON
  String FLASHCARD_FOLDERS_WITH_ONE_FLASHCARD_MODIFIED =
      """
          [
            {
              "id": "dXNlcjFAZXhhbXBsZS5jb20tZGVmYXVsdA==",
              "name": "Default",
              "owner": "user1@example.com",
              "flashcards": [
                {
                  "id": "dXNlcjFAZXhhbXBsZS5jb20tcXVlc3Rpb25h",
                  "question": "questionA",
                  "answers": ["answerA"]
                },
                {
                  "id": "dXNlcjFAZXhhbXBsZS5jb20tcXVlc3Rpb25i",
                  "question": "questionDD",
                  "answers": ["answerDD"]
                },
                {
                  "id": "dXNlcjFAZXhhbXBsZS5jb20tcXVlc3Rpb25j",
                  "question": "questionC",
                  "answers": ["answerC"]
                }
              ],
              "isOwner": true
            }
          ]
          """;

  //language=JSON
  String SYSTEM_USER_FOLDERS = """
      [
        {
          "id": "YnJvdWdodGluLXR1cmlzbQ==",
          "name": "Turism",
          "owner": "broughtin",
          "flashcards": [
            {
              "id": "YnJvdWdodGluLWV4Y3Vyc2lvbg==",
              "question": "excursion",
              "answers": [
                "wycieczka",
                "wyprawa",
                "wypad"
              ]
            }
          ],
          "isOwner": false
        }
      ]
      """;

  //language=JSON
  String FLASHCARD_FOLDERS_WITH_COPIED_FOLDER = """
      [
        {
          "id": "dXNlcjFAZXhhbXBsZS5jb20tZGVmYXVsdA==",
          "name": "Default",
          "owner": "user1@example.com",
          "flashcards": [
            {
              "id": "dXNlcjFAZXhhbXBsZS5jb20tcXVlc3Rpb25h",
              "question": "questionA",
              "answers": ["answerA"]
            },
            {
              "id": "dXNlcjFAZXhhbXBsZS5jb20tcXVlc3Rpb25i",
              "question": "questionB",
              "answers": ["answerB"]
            },
            {
              "id": "dXNlcjFAZXhhbXBsZS5jb20tcXVlc3Rpb25j",
              "question": "questionC",
              "answers": ["answerC"]
            }
          ],
          "isOwner": true
        },
        {
          "id": "dXNlcjFAZXhhbXBsZS5jb20tdHVyaXNt",
          "name": "Turism",
          "owner": "user1@example.com",
          "flashcards": [
            {
              "id": "dXNlcjFAZXhhbXBsZS5jb20tZXhjdXJzaW9u",
              "question": "excursion",
              "answers": [
                "wycieczka",
                "wyprawa",

                "wypad"
              ]
            }
          ],
          "isOwner": true
        }
      ]""";

  //language=JSON
  String FLASHCARD_FOLDERS_WITH_TURISM_FOLDER_WITH_COPIED_FOLDER = """
      [
        {
          "id": "dXNlcjFAZXhhbXBsZS5jb20tZGVmYXVsdA==",
          "name": "Default",
          "owner": "user1@example.com",
          "flashcards": [
            {
              "id": "dXNlcjFAZXhhbXBsZS5jb20tcXVlc3Rpb25h",
              "question": "questionA",
              "answers": ["answerA"]
            },
            {
              "id": "dXNlcjFAZXhhbXBsZS5jb20tcXVlc3Rpb25i",
              "question": "questionB",
              "answers": ["answerB"]
            },
            {
              "id": "dXNlcjFAZXhhbXBsZS5jb20tcXVlc3Rpb25j",
              "question": "questionC",
              "answers": ["answerC"]
            }
          ],
          "isOwner": true
        },
        {
          "id": "dXNlcjFAZXhhbXBsZS5jb20tdHVyaXNt",
          "name": "Turism",
          "owner": "user1@example.com",
          "flashcards": [
            {
              "id": "dXNlcjFAZXhhbXBsZS5jb20tcXVlc3Rpb25k",
              "question": "questionD",
              "answers": ["answerD"]
            },
            {
              "id": "dXNlcjFAZXhhbXBsZS5jb20tZXhjdXJzaW9u",
              "question": "excursion",
              "answers": [
                "wycieczka",
                "wyprawa",
                "wypad"
              ]
            }
          ],
          "isOwner": true
        }
      ]""";

}
