package com.github.xxdanielngoxx.hui.api.auth.config;

import com.github.xxdanielngoxx.hui.api.auth.helper.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;

@Configuration
public class AccessTokenConfig {

  @Bean
  public FingeringHelper fingeringHelper() {
    return new DefaultFingeringHelper();
  }

  @Bean
  public AccessTokenHelper accessTokenHelper(
      @Value("${huji.security.jwt.signing-key}") final String signingKey,
      @Value("${huji.security.jwt.expiration-in-seconds}") final Long expirationInSeconds,
      final FingeringHelper fingeringHelper) {
    return new DefaultAccessTokenHelper(signingKey, expirationInSeconds, fingeringHelper);
  }

  @Bean
  public AccessTokenAuthenticationProvider accessTokenAuthenticationProvider(
      final AccessTokenHelper accessTokenHelper) {
    return new AccessTokenAuthenticationProvider(accessTokenHelper);
  }

  @Bean
  public AuthenticationManager authenticationManager(
      final AccessTokenAuthenticationProvider accessTokenAuthenticationProvider) {
    return new ProviderManager(accessTokenAuthenticationProvider);
  }
}
