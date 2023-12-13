package com.github.damianszwed.fishky.flashcard.service.adapter.storage.entity;

import com.github.damianszwed.fishky.flashcard.service.adapter.web.resource.FlashcardFolderResource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Transient;
import lombok.Builder;
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
  @Deprecated(forRemoval = true, since = "1.2.3")
  Boolean isOwner = false;

  public FlashcardFolderResource toResource() {
    return FlashcardFolderResource
        .builder()
        .id(id)
        .name(name)
        .owner(owner)
        .flashcards(flashcards.stream().map(Flashcard::toResource).toList())
        .shares(shares.stream().map(Share::toResource).toList())
        .build();
  }
}