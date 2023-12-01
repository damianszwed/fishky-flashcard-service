package com.github.damianszwed.fishky.flashcard.service.port.flashcard;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ShareMode {
  @JsonProperty("Editor")
  EDITOR,
  @JsonProperty("View")
  VIEW
}
