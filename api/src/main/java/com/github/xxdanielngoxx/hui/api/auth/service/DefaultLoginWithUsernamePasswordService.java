package com.github.xxdanielngoxx.hui.api.auth.service;

import com.github.xxdanielngoxx.hui.api.auth.helper.*;
import jakarta.annotation.Nonnull;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultLoginWithUsernamePasswordService implements LoginWithUsernamePasswordService {

  private final AccessTokenHelper accessTokenHelper;

  private final UsernamePasswordValidator usernamePasswordValidator;

  private final FingeringHelper fingeringHelper;

  @Nonnull
  @Override
  public AccessToken login(@Nonnull final String username, @Nonnull final String password) {
    final Optional<CredentialsValidationResult> credentialsValidationResult =
        usernamePasswordValidator.validate(username, password);

    if (credentialsValidationResult.isEmpty()) {
      throw new BadCredentialsException("username or password is incorrect");
    }

    final String fingering = fingeringHelper.generate();

    final String token = accessTokenHelper.issue(credentialsValidationResult.get(), fingering);

    return AccessToken.builder().token(token).fingering(fingering).build();
  }
}
