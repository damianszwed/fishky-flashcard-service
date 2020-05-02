package com.github.damianszwed.fishky.proxy.adapter;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.junit.jupiter.api.Test;

class Base64IdEncoderDecoderTest {

  @Test
  void shouldEncodeAnyString() {
    String encodedString = new Base64IdEncoderDecoder().encode("string to encode");
    assertThat(encodedString).isEqualTo("c3RyaW5nIHRvIGVuY29kZQ==");
  }

  @Test
  void shouldDecodeBase64String() {
    String decodedString = new Base64IdEncoderDecoder().decode("c3RyaW5nIHRvIGVuY29kZQ==");
    assertThat(decodedString).isEqualTo("string to encode");
  }
}