package com.github.xxdanielngoxx.hui.api.owner.service;

import com.github.xxdanielngoxx.hui.api.auth.model.Role;
import com.github.xxdanielngoxx.hui.api.auth.model.UserEntity;
import com.github.xxdanielngoxx.hui.api.auth.service.CreateUserService;
import com.github.xxdanielngoxx.hui.api.auth.service.GetUserByIdService;
import com.github.xxdanielngoxx.hui.api.auth.service.command.CreateUserCommand;
import com.github.xxdanielngoxx.hui.api.owner.model.OwnerEntity;
import com.github.xxdanielngoxx.hui.api.owner.repository.OwnerRepository;
import com.github.xxdanielngoxx.hui.api.owner.service.command.RegisterOwnerCommand;
import jakarta.annotation.Nonnull;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultRegisteringOwnerService implements RegisteringOwnerService {

  private final CreateUserService createUserService;

  private final GetUserByIdService getUserByIdService;

  private final OwnerRepository ownerRepository;

  @Override
  @Transactional
  public UUID register(@Nonnull final RegisterOwnerCommand command) {
    final UserEntity createdUser =
        createUser(
            CreateUserCommand.builder()
                .username(command.getEmail())
                .password(command.getPassword())
                .phoneNumber(command.getPhoneNumber())
                .role(Role.OWNER)
                .build());

    final OwnerEntity ownerEntity =
        OwnerEntity.builder().fullName(command.getFullName()).user(createdUser).build();

    final OwnerEntity savedOwnerEntity = ownerRepository.save(ownerEntity);

    return savedOwnerEntity.getId();
  }

  private UserEntity createUser(@Nonnull final CreateUserCommand command) {
    final UUID createdUserId = createUserService.createUser(command);
    return getUserByIdService.getUserById(createdUserId);
  }
}
