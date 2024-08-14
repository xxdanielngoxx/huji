package com.github.xxdanielngoxx.hui.api.owner.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

import com.github.xxdanielngoxx.hui.api.owner.repository.OwnerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DefaultCheckOwnerEmailDuplicatedServiceTest {

  @InjectMocks private DefaultCheckOwnerEmailDuplicatedService service;

  @Mock private OwnerRepository ownerRepository;

  @Test
  void should_return_false_when_email_is_not_duplicated() {
    final String email = "danielngo1998@gmail.com";

    given(ownerRepository.existsByEmail(email)).willReturn(false);

    assertThat(service.checkEmailDuplicated(email)).isFalse();

    then(ownerRepository).should(times(1)).existsByEmail(email);
  }

  @Test
  void should_return_false_when_email_is_blank() {
    assertThat(service.checkEmailDuplicated("")).isFalse();
    assertThat(service.checkEmailDuplicated(null)).isFalse();
    assertThat(service.checkEmailDuplicated(" ")).isFalse();
    assertThat(service.checkEmailDuplicated("  ")).isFalse();

    verifyNoMoreInteractions(ownerRepository);
  }

  @Test
  void should_return_true_when_email_is_duplicated() {
    final String email = "danielngo1998@gmail.com";

    given(ownerRepository.existsByEmail(email)).willReturn(true);

    assertThat(service.checkEmailDuplicated(email)).isTrue();

    then(ownerRepository).should(times(1)).existsByEmail(email);
  }
}
