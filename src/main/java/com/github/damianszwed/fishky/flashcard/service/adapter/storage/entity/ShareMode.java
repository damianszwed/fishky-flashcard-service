package com.github.damianszwed.fishky.flashcard.service.adapter.storage.entity;

import com.github.damianszwed.fishky.flashcard.service.adapter.web.resource.ShareModeResource;

public enum ShareMode {
  EDITOR,
  VIEW;

  public ShareModeResource toResource() {
    return ShareModeResource.valueOf(this.name());
  }
}
