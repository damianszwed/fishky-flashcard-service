package com.github.damianszwed.fishky.flashcard.service.port.flashcard;

import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Flashcard {

  String id;
  @NotNull
  String question;
  @Builder.Default
  List<String> answers = new ArrayList<>();
}