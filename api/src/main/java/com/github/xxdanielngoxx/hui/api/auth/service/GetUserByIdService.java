package com.github.xxdanielngoxx.hui.api.auth.service;

import com.github.xxdanielngoxx.hui.api.auth.model.UserEntity;
import jakarta.annotation.Nonnull;
import java.util.UUID;

public interface GetUserByIdService {
  UserEntity getUserById(@Nonnull UUID id);
}
