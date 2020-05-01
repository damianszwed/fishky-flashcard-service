package com.github.damianszwed.fishky.proxy.configuration;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.resources;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.github.damianszwed.fishky.proxy.business.FlashcardDeleteCommandHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardServerSentEventHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardProviderFlow;
import com.github.damianszwed.fishky.proxy.business.FlashcardGetAllCommandHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardGetAllQueryHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardSaveCommandHandler;
import com.github.damianszwed.fishky.proxy.business.SwaggerHandler;
import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import com.github.damianszwed.fishky.proxy.port.flashcard.EventSource;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardStorage;
import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;


@Configuration
public class CommandQueryWebConfiguration {

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

  @Bean
  public CommandQueryHandler getAllQueryHandler(FlashcardStorage flashcardStorage) {
    return new FlashcardGetAllQueryHandler(flashcardStorage);
  }

  @Bean
  public CommandQueryHandler saveCommandHandler(FlashcardStorage flashcardStorage) {
    return new FlashcardSaveCommandHandler(flashcardStorage);
  }

  @Bean
  public CommandQueryHandler deleteCommandHandler(FlashcardStorage flashcardStorage) {
    return new FlashcardDeleteCommandHandler(flashcardStorage);
  }

  @Bean
  public CommandQueryHandler getAllCommandHandler(FlashcardProviderFlow flashcardProviderFlow) {
    return new FlashcardGetAllCommandHandler(flashcardProviderFlow);
  }

  @Bean
  public CommandQueryHandler eventHandler(EventSource eventSource) {
    return new FlashcardServerSentEventHandler(eventSource);
  }

  @Bean
  public CommandQueryHandler swaggerHandler() {
    return new SwaggerHandler();
  }

  /**
   * TODO(Damian.Szwed) is there possibility to split routes?
   */
  @Bean
  public RouterFunction<ServerResponse> flashcardRoutes(
      CommandQueryHandler getAllQueryHandler,
      CommandQueryHandler saveCommandHandler,
      CommandQueryHandler deleteCommandHandler,
      CommandQueryHandler getAllCommandHandler,
      CommandQueryHandler eventHandler,
      CommandQueryHandler swaggerHandler) {
    return route(GET("/flashcards"), getAllQueryHandler::handle)
        .andRoute(POST("/flashcards"), saveCommandHandler::handle)
        .andRoute(DELETE("/flashcards/{id}"), deleteCommandHandler::handle)
        .andRoute(GET("/getAllFlashcardsCommand"), getAllCommandHandler::handle)
        .andRoute(GET("/flashcardsEventStream"), eventHandler::handle)
        .andRoute(GET("/"), swaggerHandler::handle)
        .and(resources("/**", new ClassPathResource("static/")));
  }
}
