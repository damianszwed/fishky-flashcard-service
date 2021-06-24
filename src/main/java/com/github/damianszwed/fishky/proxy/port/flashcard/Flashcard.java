package com.github.damianszwed.fishky.proxy.port.flashcard;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
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