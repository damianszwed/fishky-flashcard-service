package com.github.damianszwed.fishky.flashcard.service.adapter.web.resource;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ShareModeResource {
  @JsonProperty("Editor")
  EDITOR,
  @JsonProperty("View")
  VIEW
}
