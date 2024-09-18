package com.github.xxdanielngoxx.hui.api.auth.service;

import jakarta.annotation.Nonnull;

public interface LoginWithUsernamePasswordService {
  @Nonnull
  AccessToken login(@Nonnull String username, @Nonnull String password);
}
