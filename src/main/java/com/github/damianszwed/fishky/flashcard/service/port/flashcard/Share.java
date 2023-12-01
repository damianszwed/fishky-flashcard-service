package com.github.damianszwed.fishky.flashcard.service.port.flashcard;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Share {

  String userId;
  ShareMode shareMode;
}
