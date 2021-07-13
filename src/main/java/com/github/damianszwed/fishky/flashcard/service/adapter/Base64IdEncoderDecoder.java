package com.github.damianszwed.fishky.flashcard.service.adapter;

import com.github.damianszwed.fishky.flashcard.service.port.IdEncoderDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64IdEncoderDecoder implements IdEncoderDecoder {

  @Override
  public String encodeId(String owner, String entityName) {
    return encode(owner + "-" + entityName.toLowerCase());
  }

  @Override
  public String encode(String toEncode) {
    return Base64.getEncoder().encodeToString(toEncode.getBytes(StandardCharsets.UTF_8));
  }

  @Override
  public String decode(String toDecode) {
    return new String(Base64.getDecoder().decode(toDecode), StandardCharsets.UTF_8);
  }
}
