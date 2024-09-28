package com.github.xxdanielngoxx.hui.api.auth.service;

import com.github.xxdanielngoxx.hui.api.auth.model.UserEntity;
import com.github.xxdanielngoxx.hui.api.auth.repository.UserRepository;
import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultGetUserByUsernameService implements GetUserByUsernameService {

  private final UserRepository userRepository;

  @Override
  public UserEntity getUserByUsername(@Nonnull String username) {
    return userRepository
        .findByUsername(username)
        .orElseThrow(
            () ->
                new EntityNotFoundException(
                    String.format("not found user with username '%s'", username)));
  }
}
