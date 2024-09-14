package com.github.xxdanielngoxx.hui.api.auth.service;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.github.xxdanielngoxx.hui.api.auth.model.UserEntity;
import com.github.xxdanielngoxx.hui.api.auth.repository.UserRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DefaultGetUserByIdServiceTest {
  @InjectMocks private DefaultGetUserByIdService service;

  @Mock private UserRepository userRepository;

  @Test
  void should_delegate_to_user_repository_when_get_user_by_id() {
    final UUID userId = UUID.randomUUID();

    final UserEntity mockUserEntity = UserEntity.builder().build();
    given(userRepository.getReferenceById(userId)).willReturn(mockUserEntity);

    service.getUserById(userId);

    then(userRepository).should(times(1)).getReferenceById(userId);
  }
}
