package com.github.xxdanielngoxx.hui.api.auth.helper;

import jakarta.annotation.Nonnull;

public interface FingeringHelper {
  int FINGERING_SIZE_IN_BYTES = 50;

  String generate();

  String hash(@Nonnull String fingering);

  boolean matches(String fingering, String hashedFingering);
}
