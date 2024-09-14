package com.github.xxdanielngoxx.hui.api.owner.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.github.xxdanielngoxx.hui.api.auth.model.Role;
import com.github.xxdanielngoxx.hui.api.auth.model.UserEntity;
import com.github.xxdanielngoxx.hui.api.auth.service.CreateUserService;
import com.github.xxdanielngoxx.hui.api.auth.service.GetUserByIdService;
import com.github.xxdanielngoxx.hui.api.auth.service.command.CreateUserCommand;
import com.github.xxdanielngoxx.hui.api.owner.model.OwnerEntity;
import com.github.xxdanielngoxx.hui.api.owner.repository.OwnerRepository;
import com.github.xxdanielngoxx.hui.api.owner.service.command.RegisterOwnerCommand;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DefaultRegisteringOwnerServiceTest {

  @InjectMocks private DefaultRegisteringOwnerService ownerService;

  @Mock private OwnerRepository ownerRepository;

  @Mock private CreateUserService createUserService;

  @Mock private GetUserByIdService getUserByIdService;

  @Test
  void should_save_owner_when_register() {
    final RegisterOwnerCommand registerOwnerCommand =
        RegisterOwnerCommand.builder()
            .fullName("Ngô Đình Lộc")
            .phoneNumber("0393238017")
            .password("<<redacted>>")
            .email("danielngo1998@gmail.com")
            .build();

    final UUID mockSavedUserId = UUID.randomUUID();
    given(createUserService.createUser(any(CreateUserCommand.class))).willReturn(mockSavedUserId);

    final UserEntity mockSavedUser = UserEntity.builder().id(mockSavedUserId).build();
    given(getUserByIdService.getUserById(mockSavedUserId)).willReturn(mockSavedUser);

    final OwnerEntity mockOwnerEntity = OwnerEntity.builder().id(UUID.randomUUID()).build();
    given(ownerRepository.save(any(OwnerEntity.class))).willReturn(mockOwnerEntity);

    final UUID ownerId = ownerService.register(registerOwnerCommand);

    assertThat(ownerId).isEqualTo(mockOwnerEntity.getId());

    // Verify interaction with CreateUserService
    final ArgumentCaptor<CreateUserCommand> createUserCommandCaptor =
        ArgumentCaptor.forClass(CreateUserCommand.class);

    then(createUserService).should(times(1)).createUser(createUserCommandCaptor.capture());

    assertThat(createUserCommandCaptor.getValue().getUsername())
        .isEqualTo(registerOwnerCommand.getEmail());

    assertThat(createUserCommandCaptor.getValue().getPassword())
        .isEqualTo(registerOwnerCommand.getPassword());

    assertThat(createUserCommandCaptor.getValue().getPhoneNumber())
        .isEqualTo(registerOwnerCommand.getPhoneNumber());

    assertThat(createUserCommandCaptor.getValue().getRole()).isEqualTo(Role.OWNER);

    // Verify interaction with GetUserByIdService
    then(getUserByIdService).should(times(1)).getUserById(mockSavedUserId);

    // Verify interaction with OwnerRepository
    final ArgumentCaptor<OwnerEntity> ownerEntityCaptor =
        ArgumentCaptor.forClass(OwnerEntity.class);

    then(ownerRepository).should(times(1)).save(ownerEntityCaptor.capture());

    assertThat(ownerEntityCaptor.getValue().getFullName())
        .isEqualTo(registerOwnerCommand.getFullName());

    assertThat(ownerEntityCaptor.getValue().getUser()).isEqualTo(mockSavedUser);
  }
}
