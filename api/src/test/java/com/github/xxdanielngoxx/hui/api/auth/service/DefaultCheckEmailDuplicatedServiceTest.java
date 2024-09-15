package com.github.xxdanielngoxx.hui.api.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

import com.github.xxdanielngoxx.hui.api.auth.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DefaultCheckEmailDuplicatedServiceTest {

  @InjectMocks private DefaultCheckEmailDuplicatedService service;

  @Mock private UserRepository userRepository;

  @Test
  void should_return_false_when_email_is_not_duplicated() {
    final String email = "danielngo1998@gmail.com";

    given(userRepository.existsByUsername(email)).willReturn(false);

    assertThat(service.checkEmailDuplicated(email)).isFalse();

    then(userRepository).should(times(1)).existsByUsername(email);
  }

  @Test
  void should_return_false_when_email_is_blank() {
    assertThat(service.checkEmailDuplicated("")).isFalse();
    assertThat(service.checkEmailDuplicated(null)).isFalse();
    assertThat(service.checkEmailDuplicated(" ")).isFalse();
    assertThat(service.checkEmailDuplicated("  ")).isFalse();

    verifyNoMoreInteractions(userRepository);
  }

  @Test
  void should_return_true_when_email_is_duplicated() {
    final String email = "danielngo1998@gmail.com";

    given(userRepository.existsByUsername(email)).willReturn(true);

    assertThat(service.checkEmailDuplicated(email)).isTrue();

    then(userRepository).should(times(1)).existsByUsername(email);
  }
}
