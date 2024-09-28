package com.github.xxdanielngoxx.hui.api.shared.config;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.xxdanielngoxx.hui.api.auth.config.AccessTokenConfig;
import com.github.xxdanielngoxx.hui.api.auth.helper.AccessTokenAuthenticationEntryPoint;
import com.github.xxdanielngoxx.hui.api.auth.helper.AccessTokenAuthenticationFilter;
import com.github.xxdanielngoxx.hui.api.auth.helper.DefaultAccessTokenResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;

@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
@Import(value = {AccessTokenConfig.class})
public class SecurityConfig {
  @Bean
  @Order(1)
  public SecurityFilterChain apiSecurityFilterChain(
      final HttpSecurity http,
      final AuthenticationManager authenticationManager,
      final ObjectMapper objectMapper)
      throws Exception {
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

    http.addFilterBefore(
        buildAccessTokenAuthenticationFilter(authenticationManager, objectMapper),
        UsernamePasswordAuthenticationFilter.class);

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

  private AccessTokenAuthenticationFilter buildAccessTokenAuthenticationFilter(
      final AuthenticationManager authenticationManager, final ObjectMapper objectMapper) {
    return new AccessTokenAuthenticationFilter(
        SecurityContextHolder.getContextHolderStrategy(),
        new RequestAttributeSecurityContextRepository(),
        new DefaultAccessTokenResolver(),
        authenticationManager,
        new AccessTokenAuthenticationEntryPoint(objectMapper),
        new WebAuthenticationDetailsSource());
  }
}
