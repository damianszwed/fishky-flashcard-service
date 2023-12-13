package com.github.damianszwed.fishky.flashcard.service.adapter.storage.entity;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Share {

  String userId;
  ShareMode shareMode;
}
