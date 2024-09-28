package com.github.xxdanielngoxx.hui.api.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verifyNoInteractions;

import com.github.xxdanielngoxx.hui.api.auth.helper.*;
import com.github.xxdanielngoxx.hui.api.auth.model.Role;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;

@ExtendWith(MockitoExtension.class)
class DefaultLoginWithUsernamePasswordServiceTest {
  @InjectMocks private DefaultLoginWithUsernamePasswordService service;

  @Mock private UsernamePasswordValidator usernamePasswordValidator;

  @Mock private FingeringHelper fingeringHelper;

  @Mock private AccessTokenHelper accessTokenHelper;

  @Test
  void should_return_access_token_when_username_and_password_are_correct() {
    final String username = "danielngo1998@gmail.com";
    final String password = "<<redacted>>";

    final CredentialsValidationResult mockUsernamePasswordValidationResult =
        CredentialsValidationResult.builder().principal(username).role(Role.OWNER).build();
    given(usernamePasswordValidator.validate(username, password))
        .willReturn(Optional.of(mockUsernamePasswordValidationResult));

    final String mockFingering = "<<fingering>>";
    given(fingeringHelper.generate()).willReturn(mockFingering);

    final String mockToken = "<<token>>";
    given(accessTokenHelper.issue(any(CredentialsValidationResult.class), eq(mockFingering)))
        .willReturn(mockToken);

    final AccessToken accessToken = service.login(username, password);
    assertThat(accessToken.token()).isEqualTo(mockToken);
    assertThat(accessToken.fingering()).isEqualTo(mockFingering);

    // Verify interaction with UsernamePasswordValidator
    then(usernamePasswordValidator).should(times(1)).validate(username, password);

    // Verify interaction with FingeringHelper
    then(fingeringHelper).should(times(1)).generate();

    // Verify interaction with AccessTokenHelper
    final ArgumentCaptor<CredentialsValidationResult> credentialsValidationResultCaptor =
        ArgumentCaptor.forClass(CredentialsValidationResult.class);

    then(accessTokenHelper)
        .should(times(1))
        .issue(credentialsValidationResultCaptor.capture(), eq(mockFingering));

    assertThat(credentialsValidationResultCaptor.getValue().principal())
        .isEqualTo(mockUsernamePasswordValidationResult.principal());

    assertThat(credentialsValidationResultCaptor.getValue().role())
        .isEqualTo(mockUsernamePasswordValidationResult.role());
  }

  @Test
  void should_throw_bad_credentials_exception_when_username_or_password_is_incorrect() {
    final String username = "danielngo1998@gmail.com";
    final String password = "<<redacted>>";

    given(usernamePasswordValidator.validate(username, password)).willReturn(Optional.empty());

    final BadCredentialsException exception =
        assertThrows(BadCredentialsException.class, () -> service.login(username, password));

    assertThat(exception.getMessage()).isEqualTo("username or password is incorrect");

    verifyNoInteractions(fingeringHelper, accessTokenHelper);
  }
}
