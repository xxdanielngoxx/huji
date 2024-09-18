package com.github.xxdanielngoxx.hui.api.shared.config;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {
  @Bean
  @Order(1)
  public SecurityFilterChain webappSecurityFilterChain(final HttpSecurity http) throws Exception {
    // spotless:off
    http.securityMatcher("/api/v1/**")
        .authorizeHttpRequests(
            authorizeHttpRequestsCustomizer ->
                authorizeHttpRequestsCustomizer
                    .requestMatchers(antMatcher(HttpMethod.POST, "/api/v1/auth/login")).permitAll()
                    .requestMatchers(antMatcher(HttpMethod.POST, "/api/v1/users/actions/check-phone-number-duplicated")).permitAll()
                    .requestMatchers(antMatcher(HttpMethod.POST, "/api/v1/users/actions/check-email-duplicated")).permitAll()
                    .requestMatchers(antMatcher(HttpMethod.POST, "/api/v1/owners")).permitAll()
                    .anyRequest().authenticated()
        )
        .csrf(csrfCustomizer -> csrfCustomizer.ignoringRequestMatchers("/api/v1/**"));
    // spotless:on

    return http.build();
  }

  @Bean
  @Order(Ordered.LOWEST_PRECEDENCE)
  public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
    // spotless:off
    http.authorizeHttpRequests(
        authorizeHttpRequestsCustomizer ->
            authorizeHttpRequestsCustomizer
                .requestMatchers("/", "/webapp/**").permitAll()
                .requestMatchers("/actuator/info", "/actuator/health").permitAll()
                .requestMatchers("/docs/**").permitAll()
                .requestMatchers("/error").permitAll()
                .anyRequest().authenticated());
    // spotless:on

    return http.build();
  }
}
