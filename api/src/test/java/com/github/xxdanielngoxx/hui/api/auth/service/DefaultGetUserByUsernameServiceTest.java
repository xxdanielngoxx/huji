package com.github.xxdanielngoxx.hui.api.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.github.xxdanielngoxx.hui.api.auth.model.UserEntity;
import com.github.xxdanielngoxx.hui.api.auth.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DefaultGetUserByUsernameServiceTest {

  @InjectMocks private DefaultGetUserByUsernameService service;

  @Mock private UserRepository userRepository;

  @Test
  void should_return_user_entity_when_found_user_by_provided_username() {
    final String username = "danielngo1998@gmai.com";

    final UserEntity mockUserEntity = UserEntity.builder().username(username).build();
    given(userRepository.findByUsername(username)).willReturn(Optional.of(mockUserEntity));

    final UserEntity foundUserEntity = service.getUserByUsername(username);

    assertThat(foundUserEntity).isEqualTo(mockUserEntity);

    then(userRepository).should(times(1)).findByUsername(username);
  }

  @Test
  void should_throw_exception_when_not_found_user_with_provided_username() {
    final String username = "danielngo1998@gmai.com";

    given(userRepository.findByUsername(username)).willReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> service.getUserByUsername(username));

    then(userRepository).should(times(1)).findByUsername(username);
  }
}
