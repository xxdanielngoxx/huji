package com.github.xxdanielngoxx.hui.api.owner.service;

import com.github.xxdanielngoxx.hui.api.owner.model.OwnerEntity;
import com.github.xxdanielngoxx.hui.api.owner.repository.OwnerRepository;
import com.github.xxdanielngoxx.hui.api.owner.service.command.RegisterOwnerCommand;
import jakarta.annotation.Nonnull;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultRegisteringOwnerService implements RegisteringOwnerService {

  private final OwnerRepository ownerRepository;

  private final PasswordEncoder passwordEncoder;

  @Override
  public UUID register(@Nonnull final RegisterOwnerCommand command) {
    final OwnerEntity ownerEntity =
        OwnerEntity.builder()
            .fullName(command.getFullName())
            .phoneNumber(command.getPhoneNumber())
            .password(passwordEncoder.encode(command.getPassword()))
            .email(command.getEmail())
            .build();

    final OwnerEntity savedOwnerEntity = ownerRepository.save(ownerEntity);

    return savedOwnerEntity.getId();
  }
}
