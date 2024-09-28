package com.github.xxdanielngoxx.hui.api.auth.helper;

import com.github.xxdanielngoxx.hui.api.auth.model.Role;
import org.springframework.security.core.AuthenticatedPrincipal;

public record AccessTokenAuthenticatedPrincipal(String subject, Role role)
    implements AuthenticatedPrincipal {
  @Override
  public String getName() {
    return subject;
  }
}
