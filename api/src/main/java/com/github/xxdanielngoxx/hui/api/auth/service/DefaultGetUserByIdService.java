package com.github.xxdanielngoxx.hui.api.auth.service;

import com.github.xxdanielngoxx.hui.api.auth.model.UserEntity;
import com.github.xxdanielngoxx.hui.api.auth.repository.UserRepository;
import jakarta.annotation.Nonnull;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultGetUserByIdService implements GetUserByIdService {

  private final UserRepository userRepository;

  @Override
  public UserEntity getUserById(@Nonnull final UUID id) {
    return userRepository.getReferenceById(id);
  }
}
