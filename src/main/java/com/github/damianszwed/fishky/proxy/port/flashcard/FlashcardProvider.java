package com.github.damianszwed.fishky.proxy.port.flashcard;

import java.util.List;

public interface FlashcardProvider {

  List<Flashcard> getFlashcards(String username);
}