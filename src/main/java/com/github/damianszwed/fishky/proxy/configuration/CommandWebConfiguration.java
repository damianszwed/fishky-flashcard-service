package com.github.damianszwed.fishky.proxy.configuration;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.resources;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.github.damianszwed.fishky.proxy.application.FlashcardProviderFlow;
import com.github.damianszwed.fishky.proxy.application.GetAllCommand;
import com.github.damianszwed.fishky.proxy.application.SwaggerCommand;
import com.github.damianszwed.fishky.proxy.port.CommandHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurerComposite;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@EnableWebFlux
public class CommandWebConfiguration {

  @Bean
  public WebFluxConfigurer corsConfigurer() {
    return new WebFluxConfigurerComposite() {

      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*")
            .allowedMethods("*");
      }
    };
  }

  @Bean
  public CommandHandler swaggerHandler() {
    return new SwaggerCommand();
  }

  @Bean
  public CommandHandler getAllCommand(
      FlashcardProviderFlow flashcardProviderFlow) {
    return new GetAllCommand(flashcardProviderFlow);
  }

  @Bean
  public RouterFunction<ServerResponse> routes(
      CommandHandler getAllCommand,
      CommandHandler swaggerHandler) {
    return route(GET("/flashcards"), getAllCommand::handle)
        .andRoute(GET("/"), swaggerHandler::handle)
        .and(resources("/**", new ClassPathResource("static/")));
  }
}
