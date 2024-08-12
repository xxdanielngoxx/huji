package com.github.xxdanielngoxx.hui.api.shared.config;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {
  @Bean
  public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
    // spotless:off
      http
            .authorizeHttpRequests(
                authorizeHttpRequestsCustomizer ->
                    authorizeHttpRequestsCustomizer
                            .requestMatchers("/", "/webapp/**").permitAll()
                            .requestMatchers("/actuator/info", "/actuator/health").permitAll()
                            .requestMatchers("/docs/**").permitAll()
                            .requestMatchers(antMatcher(HttpMethod.POST, "/api/auth/login")).permitAll()
                            .requestMatchers(antMatcher(HttpMethod.POST, "/api/owners")).permitAll()
                            .requestMatchers(antMatcher(HttpMethod.GET, "/api/owners/checkPhoneNumberNotYetUsed")).permitAll()
                            .requestMatchers("/error").permitAll()
                            .anyRequest().authenticated()
            )
            .csrf(csrfCustomizer -> csrfCustomizer.ignoringRequestMatchers("/api/**"));
      // spotless:on

    return http.build();
  }
}
