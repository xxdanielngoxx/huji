package com.github.xxdanielngoxx.hui.api.user.service;

import com.github.xxdanielngoxx.hui.api.user.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultCheckOwnerPhoneNumberDuplicatedService
    implements CheckOwnerPhoneNumberDuplicatedService {

  private final OwnerRepository ownerRepository;

  @Override
  public boolean checkPhoneNumberDuplicated(final String phoneNumber) {
    if (StringUtils.isBlank(phoneNumber)) {
      return false;
    }

    return ownerRepository.existsByPhoneNumber(phoneNumber);
  }
}
