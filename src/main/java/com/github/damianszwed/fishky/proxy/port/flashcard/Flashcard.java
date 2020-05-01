package com.github.damianszwed.fishky.proxy.port.flashcard;

import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Flashcard {

  String id;
  @NotNull
  String question;
  @NotNull
  String answer;
}