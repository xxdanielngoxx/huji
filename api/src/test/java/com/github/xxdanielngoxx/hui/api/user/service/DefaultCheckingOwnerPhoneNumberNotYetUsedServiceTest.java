package com.github.xxdanielngoxx.hui.api.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.github.xxdanielngoxx.hui.api.user.repository.OwnerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DefaultCheckingOwnerPhoneNumberNotYetUsedServiceTest {

  @InjectMocks private DefaultCheckingOwnerPhoneNumberNotYetUsedService service;

  @Mock private OwnerRepository ownerRepository;

  @Test
  void should_do_nothing_when_phone_number_is_not_used_by_any_owner() {
    final String phoneNumber = "0393238017";

    given(ownerRepository.existsByPhoneNumber(phoneNumber)).willReturn(false);

    service.checkPhoneNumberNotYetUsed(phoneNumber);

    then(ownerRepository).should(times(1)).existsByPhoneNumber(phoneNumber);
  }

  @Test
  void should_throw_exception_when_phone_number_is_used_by_another_owner() {
    final String phoneNumber = "0393238017";

    given(ownerRepository.existsByPhoneNumber(phoneNumber)).willReturn(true);

    final IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class, () -> service.checkPhoneNumberNotYetUsed(phoneNumber));

    assertThat(exception.getMessage())
        .contains(String.format("phone number %s is already used by another owner", phoneNumber));

    then(ownerRepository).should(times(1)).existsByPhoneNumber(phoneNumber);
  }
}
