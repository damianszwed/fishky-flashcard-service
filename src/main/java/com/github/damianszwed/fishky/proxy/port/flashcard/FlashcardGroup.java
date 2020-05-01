package com.github.damianszwed.fishky.proxy.port.flashcard;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Value
@Document
@Builder(toBuilder = true)
public class FlashcardGroup {

  @Id
  String id;

  @NotNull
  String name;

  String owner;

  @Builder.Default
  List<Flashcard> flashcards = new ArrayList<>();
}