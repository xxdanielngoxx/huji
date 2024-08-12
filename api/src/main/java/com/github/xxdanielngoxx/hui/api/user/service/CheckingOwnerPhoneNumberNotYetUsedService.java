package com.github.xxdanielngoxx.hui.api.user.service;

import jakarta.annotation.Nonnull;

public interface CheckingOwnerPhoneNumberNotYetUsedService {
  void checkPhoneNumberNotYetUsed(@Nonnull final String phoneNumber);
}
