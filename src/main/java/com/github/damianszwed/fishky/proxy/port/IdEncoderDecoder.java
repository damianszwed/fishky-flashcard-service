package com.github.damianszwed.fishky.proxy.port;

public interface IdEncoderDecoder {

  /**
   * Generates id.
   * @param owner      owner of the entity
   * @param entityName could be flashcardFolder name OR flashcard question. Will be lowerCased.
   * @return encoded by Base64 id
   */
  String encodeId(String owner, String entityName);

  String encode(String toEncode);

  String decode(String toDecode);
}
