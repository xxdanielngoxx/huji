package com.github.xxdanielngoxx.hui.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {
  @Bean
  public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
    // spotless:off
    return http.authorizeHttpRequests(
            authorizeHttpRequestsCustomizer ->
                authorizeHttpRequestsCustomizer
                    .requestMatchers("/", "/webapp/**").permitAll()
                    .requestMatchers("/actuator/info", "/actuator/health").permitAll()
                    .requestMatchers("/error").permitAll()
                    .anyRequest().authenticated()
        )
        .build();
    // spotless:on
  }
}
