package com.github.xxdanielngoxx.hui.api.auth.helper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.annotation.Nonnull;

public interface AccessTokenHelper {
  String ISSUER = "huji-api";

  long DEFAULT_EXPIRATION_IN_SECONDS = 3600;

  String ROLE_CLAIM_NAME = "role";

  String FINGERING_CLAIM_NAME = "fingering";

  String issue(
      @Nonnull CredentialsValidationResult credentialsValidationResult, @Nonnull String fingering);

  Jws<Claims> verify(@Nonnull String token, @Nonnull String fingering);
}
