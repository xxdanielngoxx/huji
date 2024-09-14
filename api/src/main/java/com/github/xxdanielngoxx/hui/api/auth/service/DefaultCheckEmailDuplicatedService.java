package com.github.xxdanielngoxx.hui.api.auth.service;

import com.github.xxdanielngoxx.hui.api.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultCheckEmailDuplicatedService implements CheckEmailDuplicatedService {

  private final UserRepository userRepository;

  @Override
  public boolean checkEmailDuplicated(final String email) {
    if (StringUtils.isBlank(email)) {
      return false;
    }

    return userRepository.existsByUsername(email);
  }
}
