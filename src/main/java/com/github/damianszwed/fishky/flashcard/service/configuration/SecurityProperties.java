package com.github.damianszwed.fishky.flashcard.service.configuration;

import java.util.Collections;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("fishky")
@Data
public class SecurityProperties {

  private String systemUserExternalId = "broughtin";
  private String systemUserInternalId = "broughtin";

  private Cors cors = new Cors();

  @Data
  public static class Cors {
    private List<String> allowedOrigins = Collections.singletonList("*");
    private List<String> allowedHeaders = Collections.singletonList("*");
    private Boolean allowCredentials = Boolean.TRUE;
  }

}
