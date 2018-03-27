package com.github.damianszwed.fishky.proxy.port.flashcard;

import org.springframework.data.annotation.Id;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Flashcard {
    @Id String id;
    String question;
    String answer;
}
