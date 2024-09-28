package com.github.xxdanielngoxx.hui.api.auth.helper;

import jakarta.annotation.Nonnull;
import java.util.Collection;
import java.util.Set;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class AccessTokenAuthenticatedToken extends AbstractAuthenticationToken {

  private final Object principal;

  public AccessTokenAuthenticatedToken(
      @Nonnull final AccessTokenAuthenticatedPrincipal principal, final Object details) {
    super(extractGrantedAuthorities(principal));
    setAuthenticated(true);
    setDetails(details);

    this.principal = principal;
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return principal;
  }

  private static Collection<? extends GrantedAuthority> extractGrantedAuthorities(
      @Nonnull final AccessTokenAuthenticatedPrincipal principal) {
    return Set.of(new SimpleGrantedAuthority(principal.role().name()));
  }
}
