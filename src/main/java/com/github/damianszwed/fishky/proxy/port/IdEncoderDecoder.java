package com.github.damianszwed.fishky.proxy.port;

public interface IdEncoderDecoder {

  String encode(String toEncode);

  String decode(String toDecode);
}
