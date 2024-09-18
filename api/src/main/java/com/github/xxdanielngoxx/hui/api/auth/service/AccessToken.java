package com.github.xxdanielngoxx.hui.api.auth.service;

import jakarta.annotation.Nonnull;
import lombok.Builder;

@Builder
public record AccessToken(@Nonnull String token, @Nonnull String fingering) {}
