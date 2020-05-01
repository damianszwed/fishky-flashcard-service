package com.github.damianszwed.fishky.proxy.configuration;

import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
public class CorsFilterConfiguration {

  @Bean
  public WebFilter corsFilter() {
    return (ServerWebExchange ctx, WebFilterChain chain) -> Optional.of(ctx.getRequest())
        .filter(CorsUtils::isCorsRequest)
        .map(request -> {
          ServerHttpResponse response1 = ctx.getResponse();
          HttpHeaders headers = response1.getHeaders();
          headers.add("Access-Control-Allow-Origin", "*");
          headers.add("Access-Control-Allow-Methods",
              "GET, PUT, POST, DELETE, OPTIONS");
          headers.add("Access-Control-Max-Age", "3600");
          headers.add("Access-Control-Allow-Headers",
              "x-requested-with, authorization, Content-Type, "
                  + "Authorization, credential, X-XSRF-TOKEN");
          return request;
        })
        .filter(serverHttpRequest -> serverHttpRequest.getMethod() == HttpMethod.OPTIONS)
        .<Mono<Void>>map(serverHttpRequests -> {
          ServerHttpResponse response = ctx.getResponse();
          response.setStatusCode(HttpStatus.OK);
          return Mono.empty();
        })
        .orElseGet(() -> chain.filter(ctx));
  }

}
