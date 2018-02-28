package com.github.damianszwed.fishky.proxy.greeting;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class Greeting {
    private final long id;
    private final String content;
}
