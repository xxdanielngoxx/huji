package com.github.xxdanielngoxx.hui.api.owner.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verifyNoInteractions;

import com.github.xxdanielngoxx.hui.api.owner.repository.OwnerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DefaultCheckOwnerPhoneNumberDuplicatedServiceTest {

  @InjectMocks private DefaultCheckOwnerPhoneNumberDuplicatedService service;

  @Mock private OwnerRepository ownerRepository;

  @Test
  void should_return_true_when_phone_number_is_not_duplicated() {
    final String phoneNumber = "0393238017";

    given(ownerRepository.existsByPhoneNumber(phoneNumber)).willReturn(false);

    assertThat(service.checkPhoneNumberDuplicated(phoneNumber)).isFalse();

    then(ownerRepository).should(times(1)).existsByPhoneNumber(phoneNumber);
  }

  @Test
  void should_return_false_when_phone_number_is_blank() {
    assertThat(service.checkPhoneNumberDuplicated(null)).isFalse();
    assertThat(service.checkPhoneNumberDuplicated("")).isFalse();
    assertThat(service.checkPhoneNumberDuplicated(" ")).isFalse();
    assertThat(service.checkPhoneNumberDuplicated("  ")).isFalse();

    verifyNoInteractions(ownerRepository);
  }

  @Test
  void should_return_true_when_phone_number_is_duplicated() {
    final String phoneNumber = "0393238017";

    given(ownerRepository.existsByPhoneNumber(phoneNumber)).willReturn(true);

    assertThat(service.checkPhoneNumberDuplicated(phoneNumber)).isTrue();

    then(ownerRepository).should(times(1)).existsByPhoneNumber(phoneNumber);
  }
}
