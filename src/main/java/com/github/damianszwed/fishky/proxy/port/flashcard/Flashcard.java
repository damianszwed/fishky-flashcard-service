package com.github.damianszwed.fishky.proxy.port.flashcard;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Flashcard {
    String id;
    String question;
    String answer;
}
