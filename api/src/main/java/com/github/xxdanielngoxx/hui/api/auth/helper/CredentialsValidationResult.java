package com.github.xxdanielngoxx.hui.api.auth.helper;

import com.github.xxdanielngoxx.hui.api.auth.model.Role;
import jakarta.annotation.Nonnull;
import lombok.Builder;

@Builder
public record CredentialsValidationResult(@Nonnull String principal, @Nonnull Role role) {}
