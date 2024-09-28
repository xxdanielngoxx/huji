package com.github.xxdanielngoxx.hui.api.auth.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class AccessTokenAuthenticationProvider implements AuthenticationProvider {

  private final Logger logger = LoggerFactory.getLogger(AccessTokenAuthenticationProvider.class);

  private final AccessTokenHelper accessTokenHelper;

  public AccessTokenAuthenticationProvider(final AccessTokenHelper accessTokenHelper) {
    this.accessTokenHelper = accessTokenHelper;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    final AccessTokenAuthenticationToken bearerToken =
        (AccessTokenAuthenticationToken) authentication;

    final AccessTokenAuthenticatedPrincipal principal =
        accessTokenHelper.verify(bearerToken.getToken(), bearerToken.getFingering());

    return new AccessTokenAuthenticatedToken(principal, bearerToken.getDetails());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return AccessTokenAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
