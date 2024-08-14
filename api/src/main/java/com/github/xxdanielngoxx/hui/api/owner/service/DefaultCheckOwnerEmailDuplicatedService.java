package com.github.xxdanielngoxx.hui.api.owner.service;

import com.github.xxdanielngoxx.hui.api.owner.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultCheckOwnerEmailDuplicatedService implements CheckOwnerEmailDuplicatedService {

  private final OwnerRepository ownerRepository;

  @Override
  public boolean checkEmailDuplicated(final String email) {
    if (StringUtils.isBlank(email)) {
      return false;
    }

    return ownerRepository.existsByEmail(email);
  }
}
