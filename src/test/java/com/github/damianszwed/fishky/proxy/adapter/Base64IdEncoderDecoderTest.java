package com.github.damianszwed.fishky.proxy.adapter;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.junit.jupiter.api.Test;

class Base64IdEncoderDecoderTest {

  @Test
  void shouldGenerateId() {
    String generatedId = new Base64IdEncoderDecoder().encodeId("someOwner", "someEntity");
    assertThat(generatedId).isEqualTo("c29tZU93bmVyLXNvbWVlbnRpdHk=");
  }

  @Test
  void shouldEncodeAnyString() {
    String encodedString = new Base64IdEncoderDecoder().encode("someOwner-someentity");
    assertThat(encodedString).isEqualTo("c29tZU93bmVyLXNvbWVlbnRpdHk=");
  }

  @Test
  void shouldDecodeBase64String() {
    String decodedString = new Base64IdEncoderDecoder().decode("c29tZU93bmVyLXNvbWVlbnRpdHk=");
    assertThat(decodedString).isEqualTo("someOwner-someentity");
  }
}