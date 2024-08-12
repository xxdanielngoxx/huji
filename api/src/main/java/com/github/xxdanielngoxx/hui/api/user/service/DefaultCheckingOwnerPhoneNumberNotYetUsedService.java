package com.github.xxdanielngoxx.hui.api.user.service;

import com.github.xxdanielngoxx.hui.api.user.repository.OwnerRepository;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultCheckingOwnerPhoneNumberNotYetUsedService
    implements CheckingOwnerPhoneNumberNotYetUsedService {

  private final OwnerRepository ownerRepository;

  @Override
  public void checkPhoneNumberNotYetUsed(@Nonnull String phoneNumber) {
    if (ownerRepository.existsByPhoneNumber(phoneNumber)) {
      throw new IllegalArgumentException(
          String.format("phone number %s is already used by another owner", phoneNumber));
    }
  }
}
