package com.github.xxdanielngoxx.hui.api.auth.helper;

import jakarta.annotation.Nonnull;
import java.util.Collections;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

@Getter
public class AccessTokenAuthenticationToken extends AbstractAuthenticationToken {

  private final String token;

  private final String fingering;

  public AccessTokenAuthenticationToken(
      @Nonnull final String token, final String fingering, final Object details) {
    super(Collections.emptyList());
    setDetails(details);

    this.token = token;
    this.fingering = fingering;
  }

  @Override
  public Object getCredentials() {
    return this.fingering;
  }

  @Override
  public Object getPrincipal() {
    return this.token;
  }
}
