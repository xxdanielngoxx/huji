package com.github.xxdanielngoxx.hui.api.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verifyNoInteractions;

import com.github.xxdanielngoxx.hui.api.auth.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DefaultCheckPhoneNumberDuplicatedServiceTest {

  @InjectMocks private DefaultCheckPhoneNumberDuplicatedService service;

  @Mock private UserRepository userRepository;

  @Test
  void should_return_true_when_phone_number_is_not_duplicated() {
    final String phoneNumber = "0393238017";

    given(userRepository.existsByPhoneNumber(phoneNumber)).willReturn(false);

    assertThat(service.checkPhoneNumberDuplicated(phoneNumber)).isFalse();

    then(userRepository).should(times(1)).existsByPhoneNumber(phoneNumber);
  }

  @Test
  void should_return_false_when_phone_number_is_blank() {
    assertThat(service.checkPhoneNumberDuplicated(null)).isFalse();
    assertThat(service.checkPhoneNumberDuplicated("")).isFalse();
    assertThat(service.checkPhoneNumberDuplicated(" ")).isFalse();
    assertThat(service.checkPhoneNumberDuplicated("  ")).isFalse();

    verifyNoInteractions(userRepository);
  }

  @Test
  void should_return_true_when_phone_number_is_duplicated() {
    final String phoneNumber = "0393238017";

    given(userRepository.existsByPhoneNumber(phoneNumber)).willReturn(true);

    assertThat(service.checkPhoneNumberDuplicated(phoneNumber)).isTrue();

    then(userRepository).should(times(1)).existsByPhoneNumber(phoneNumber);
  }
}
