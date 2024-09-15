package com.github.xxdanielngoxx.hui.api.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.github.xxdanielngoxx.hui.api.auth.model.Role;
import com.github.xxdanielngoxx.hui.api.auth.model.UserEntity;
import com.github.xxdanielngoxx.hui.api.auth.repository.UserRepository;
import com.github.xxdanielngoxx.hui.api.auth.service.command.CreateUserCommand;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class DefaultCreateUserServiceTest {

  @InjectMocks private DefaultCreateUserService createUserService;

  @Mock private UserRepository userRepository;

  @Mock private PasswordEncoder passwordEncoder;

  @Test
  void should_create_user_success() {
    final CreateUserCommand command =
        CreateUserCommand.builder()
            .username("danielngo1998@gmail.com")
            .password("<<redacted>>")
            .phoneNumber("0393238017")
            .role(Role.OWNER)
            .build();

    final String mockEncodedPassword = "<<redacted>>:<<encoded>>";
    given(passwordEncoder.encode(command.getPassword())).willReturn(mockEncodedPassword);

    final UserEntity mockUserEntity = UserEntity.builder().id(UUID.randomUUID()).build();
    given(userRepository.save(any(UserEntity.class))).willReturn(mockUserEntity);

    final UUID createdUserId = createUserService.createUser(command);

    assertThat(createdUserId).isEqualTo(mockUserEntity.getId());

    final ArgumentCaptor<UserEntity> userEntityArgumentCaptor =
        ArgumentCaptor.forClass(UserEntity.class);
    then(userRepository).should(times(1)).save(userEntityArgumentCaptor.capture());
    assertThat(userEntityArgumentCaptor.getValue().getUsername()).isEqualTo(command.getUsername());
    assertThat(userEntityArgumentCaptor.getValue().getPassword()).isEqualTo(mockEncodedPassword);
    assertThat(userEntityArgumentCaptor.getValue().getPhoneNumber())
        .isEqualTo(command.getPhoneNumber());
    assertThat(userEntityArgumentCaptor.getValue().getRole()).isEqualTo(command.getRole());
  }
}
