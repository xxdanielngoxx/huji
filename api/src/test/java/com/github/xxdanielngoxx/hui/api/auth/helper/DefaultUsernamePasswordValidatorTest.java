package com.github.xxdanielngoxx.hui.api.auth.helper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verifyNoInteractions;

import com.github.xxdanielngoxx.hui.api.auth.model.Role;
import com.github.xxdanielngoxx.hui.api.auth.model.UserEntity;
import com.github.xxdanielngoxx.hui.api.auth.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class DefaultUsernamePasswordValidatorTest {

  @InjectMocks private DefaultUsernamePasswordValidator validator;

  @Mock private UserRepository userRepository;

  @Mock private PasswordEncoder passwordEncoder;

  @Test
  void should_return_credentials_validation_result_when_username_and_password_are_match() {
    final String username = "danielngo1998@gmail.com";
    final String password = "<<redacted>>";

    final UserEntity mockUser =
        UserEntity.builder()
            .username(username)
            .password("<<redacted>>:<<encoded>>")
            .role(Role.OWNER)
            .build();
    given(userRepository.findByUsernameOrPhoneNumber(username)).willReturn(Optional.of(mockUser));

    given(passwordEncoder.matches(password, mockUser.getPassword())).willReturn(true);

    final Optional<CredentialsValidationResult> result = validator.validate(username, password);
    assertThat(result).isNotEmpty();
    assertThat(result.get().principal()).isEqualTo(mockUser.getUsername());
    assertThat(result.get().role()).isEqualTo(mockUser.getRole());

    then(userRepository).should(times(1)).findByUsernameOrPhoneNumber(username);

    then(passwordEncoder).should(times(1)).matches(password, mockUser.getPassword());
  }

  @Test
  void should_return_empty_when_username_is_not_exist() {
    final String username = "danielngo1998@gmail.com";
    final String password = "<<redacted>>";

    given(userRepository.findByUsernameOrPhoneNumber(username)).willReturn(Optional.empty());

    final Optional<CredentialsValidationResult> result = validator.validate(username, password);
    assertThat(result).isEmpty();

    then(userRepository).should(times(1)).findByUsernameOrPhoneNumber(username);

    verifyNoInteractions(passwordEncoder);
  }

  @Test
  void should_empty_when_password_is_not_match() {
    final String username = "danielngo1998@gmail.com";
    final String password = "<<redacted>>";

    final UserEntity mockUser =
        UserEntity.builder()
            .username(username)
            .password("<<redacted>>:<<encoded>>")
            .role(Role.OWNER)
            .build();
    given(userRepository.findByUsernameOrPhoneNumber(username)).willReturn(Optional.of(mockUser));

    given(passwordEncoder.matches(password, mockUser.getPassword())).willReturn(false);

    final Optional<CredentialsValidationResult> result = validator.validate(username, password);
    assertThat(result).isEmpty();

    then(userRepository).should(times(1)).findByUsernameOrPhoneNumber(username);

    then(passwordEncoder).should(times(1)).matches(password, mockUser.getPassword());
  }
}
