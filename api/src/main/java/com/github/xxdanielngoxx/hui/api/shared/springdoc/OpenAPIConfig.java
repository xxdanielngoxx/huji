package com.github.xxdanielngoxx.hui.api.shared.springdoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class OpenAPIConfig {

  public static final String BEARER_SECURITY_SCHEMA_KEY = "bearer";

  @Bean
  public OpenAPI openAPI(final BuildProperties buildProperties) {
    final Info info = new Info().title("Huji API").version(buildProperties.getVersion());

    final SecurityScheme bearerScheme =
        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT");

    final Components components =
        new Components().addSecuritySchemes(BEARER_SECURITY_SCHEMA_KEY, bearerScheme);

    return new OpenAPI().components(components).info(info);
  }
}
