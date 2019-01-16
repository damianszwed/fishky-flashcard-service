package com.github.damianszwed.fishky.proxy.configuration;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.resources;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.github.damianszwed.fishky.proxy.business.DeleteCommandHandler;
import com.github.damianszwed.fishky.proxy.business.EventHandler;
import com.github.damianszwed.fishky.proxy.business.FlashcardProviderFlow;
import com.github.damianszwed.fishky.proxy.business.GetAllCommandHandler;
import com.github.damianszwed.fishky.proxy.business.GetAllQueryHandler;
import com.github.damianszwed.fishky.proxy.business.SaveCommandHandler;
import com.github.damianszwed.fishky.proxy.business.SwaggerHandler;
import com.github.damianszwed.fishky.proxy.port.CommandQueryHandler;
import com.github.damianszwed.fishky.proxy.port.flashcard.EventSource;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardProvider;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardRemover;
import com.github.damianszwed.fishky.proxy.port.flashcard.FlashcardSaver;
import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;


@Configuration
@EnableWebFlux
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
  public CommandQueryHandler getAllQueryHandler(FlashcardProvider flashcardProvider) {
    return new GetAllQueryHandler(flashcardProvider);
  }

  @Bean
  public CommandQueryHandler saveCommandHandler(
      FlashcardSaver flashcardSaver) {
    return new SaveCommandHandler(flashcardSaver);
  }

  @Bean
  public CommandQueryHandler deleteCommandHandler(
      FlashcardRemover flashcardRemover) {
    return new DeleteCommandHandler(flashcardRemover);
  }

  @Bean
  public CommandQueryHandler getAllCommandHandler(
      FlashcardProviderFlow flashcardProviderFlow) {
    return new GetAllCommandHandler(flashcardProviderFlow);
  }

  @Bean
  public CommandQueryHandler eventHandler(EventSource eventSource) {
    return new EventHandler(eventSource);
  }

  @Bean
  public CommandQueryHandler swaggerHandler() {
    return new SwaggerHandler();
  }

  @Bean
  public RouterFunction<ServerResponse> routes(
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
