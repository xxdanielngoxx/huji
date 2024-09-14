package com.github.xxdanielngoxx.hui.api.auth.service;

import com.github.xxdanielngoxx.hui.api.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultCheckPhoneNumberDuplicatedService implements CheckPhoneNumberDuplicatedService {

  private final UserRepository userRepository;

  @Override
  public boolean checkPhoneNumberDuplicated(final String phoneNumber) {
    if (StringUtils.isBlank(phoneNumber)) {
      return false;
    }

    return userRepository.existsByPhoneNumber(phoneNumber);
  }
}
