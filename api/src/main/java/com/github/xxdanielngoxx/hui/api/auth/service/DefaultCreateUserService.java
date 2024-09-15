package com.github.xxdanielngoxx.hui.api.auth.service;

import com.github.xxdanielngoxx.hui.api.auth.model.UserEntity;
import com.github.xxdanielngoxx.hui.api.auth.repository.UserRepository;
import com.github.xxdanielngoxx.hui.api.auth.service.command.CreateUserCommand;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultCreateUserService implements CreateUserService {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  @Override
  public UUID createUser(CreateUserCommand command) {
    final UserEntity userEntity =
        UserEntity.builder()
            .username(command.getUsername())
            .password(passwordEncoder.encode(command.getPassword()))
            .phoneNumber(command.getPhoneNumber())
            .role(command.getRole())
            .build();

    final UserEntity savedUserEntity = userRepository.save(userEntity);

    return savedUserEntity.getId();
  }
}
