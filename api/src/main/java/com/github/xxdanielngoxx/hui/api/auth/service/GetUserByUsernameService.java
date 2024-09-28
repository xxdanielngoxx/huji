package com.github.xxdanielngoxx.hui.api.auth.service;

import com.github.xxdanielngoxx.hui.api.auth.model.UserEntity;
import jakarta.annotation.Nonnull;

public interface GetUserByUsernameService {
  UserEntity getUserByUsername(@Nonnull String username);
}
