package com.github.damianszwed.fishky.proxy.port.flashcard;

import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Value
@Document
@Builder(toBuilder = true)
public class Flashcard {

  @Id
  String id;
  String question;
  String answer;
}