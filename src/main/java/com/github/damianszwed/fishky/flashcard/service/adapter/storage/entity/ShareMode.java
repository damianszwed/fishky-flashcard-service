package com.github.damianszwed.fishky.flashcard.service.adapter.storage.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ShareMode {
  @JsonProperty("Editor")
  EDITOR,
  @JsonProperty("View")
  VIEW
}
