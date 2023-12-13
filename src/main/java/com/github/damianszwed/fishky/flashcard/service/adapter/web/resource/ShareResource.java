package com.github.damianszwed.fishky.flashcard.service.adapter.web.resource;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class ShareResource {

  String userId;
  ShareModeResource shareMode;
}
