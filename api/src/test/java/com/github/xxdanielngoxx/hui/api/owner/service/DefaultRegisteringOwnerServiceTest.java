package com.github.xxdanielngoxx.hui.api.owner.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

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
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class DefaultRegisteringOwnerServiceTest {

  @InjectMocks private DefaultRegisteringOwnerService ownerService;

  @Mock private OwnerRepository ownerRepository;

  @Mock private PasswordEncoder passwordEncoder;

  @Test
  void should_save_owner_when_register() {
    final RegisterOwnerCommand command =
        RegisterOwnerCommand.builder()
            .fullName("Ngô Đình Lộc")
            .phoneNumber("0393238017")
            .password("super_secret?#")
            .email("danielngo1998@gmail.com")
            .build();

    final OwnerEntity mockOwnerEntity = OwnerEntity.builder().id(UUID.randomUUID()).build();
    given(ownerRepository.save(any(OwnerEntity.class))).willReturn(mockOwnerEntity);

    final String mockEncodedPassword = "super_secret_?#_encoded";
    given(passwordEncoder.encode(command.getPassword())).willReturn(mockEncodedPassword);

    final UUID ownerId = ownerService.register(command);

    assertThat(ownerId).isEqualTo(mockOwnerEntity.getId());

    then(passwordEncoder).should(times(1)).encode(command.getPassword());

    final ArgumentCaptor<OwnerEntity> ownerEntityCaptor =
        ArgumentCaptor.forClass(OwnerEntity.class);
    then(ownerRepository).should(times(1)).save(ownerEntityCaptor.capture());
    assertThat(ownerEntityCaptor.getValue().getFullName()).isEqualTo(command.getFullName());
    assertThat(ownerEntityCaptor.getValue().getPhoneNumber()).isEqualTo(command.getPhoneNumber());
    assertThat(ownerEntityCaptor.getValue().getPassword()).isEqualTo(mockEncodedPassword);
    assertThat(ownerEntityCaptor.getValue().getEmail()).isEqualTo(command.getEmail());
  }
}
