package com.github.xxdanielngoxx.hui.api.auth.helper;

import jakarta.annotation.Nonnull;
import java.util.Optional;

public interface UsernamePasswordValidator {
  Optional<CredentialsValidationResult> validate(
      @Nonnull String username, @Nonnull String password);
}
