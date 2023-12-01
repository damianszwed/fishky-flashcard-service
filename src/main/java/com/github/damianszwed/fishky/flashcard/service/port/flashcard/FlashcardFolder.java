package com.github.damianszwed.fishky.flashcard.service.port.flashcard;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Transient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Value
@Document
@Builder(toBuilder = true)
public class FlashcardFolder {

  @Id
  String id;

  @NotNull
  @NotBlank
  String name;

  String owner;

  @Builder.Default
  List<Flashcard> flashcards = new ArrayList<>();

  @Builder.Default
  List<Share> shares = new ArrayList<>();

  @Transient
  @Builder.Default
  Boolean isOwner = false;
}