package com.github.xxdanielngoxx.hui.api.auth.helper;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface AccessTokenResolver {
  Optional<AccessToken> resolve(HttpServletRequest request);
}
