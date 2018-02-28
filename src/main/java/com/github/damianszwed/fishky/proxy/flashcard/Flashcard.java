package com.github.damianszwed.fishky.proxy.flashcard;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Flashcard {
    String question;
    String answer;
}
