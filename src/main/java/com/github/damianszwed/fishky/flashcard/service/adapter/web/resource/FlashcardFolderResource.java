package com.github.damianszwed.fishky.flashcard.service.adapter.web.resource;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class FlashcardFolderResource {

  String id;

  @NotNull
  @NotBlank
  String name;

  String owner;

  @Builder.Default
  List<FlashcardResource> flashcards = new ArrayList<>();

  @Builder.Default
  List<ShareResource> shares = new ArrayList<>();

  @Builder.Default
  Boolean isOwner = false;
}