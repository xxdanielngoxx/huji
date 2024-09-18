package com.github.xxdanielngoxx.hui.api.auth.helper;

import com.github.xxdanielngoxx.hui.api.auth.model.UserEntity;
import com.github.xxdanielngoxx.hui.api.auth.repository.UserRepository;
import jakarta.annotation.Nonnull;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultUsernamePasswordValidator implements UsernamePasswordValidator {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  @Override
  public Optional<CredentialsValidationResult> validate(
      @Nonnull String username, @Nonnull String password) {
    final Optional<UserEntity> user = userRepository.findByUsernameOrPhoneNumber(username);

    if (user.isEmpty()) {
      return Optional.empty();
    }

    if (!passwordEncoder.matches(password, user.get().getPassword())) {
      return Optional.empty();
    }

    final CredentialsValidationResult result =
        CredentialsValidationResult.builder()
            .principal(user.get().getUsername())
            .role(user.get().getRole())
            .build();

    return Optional.of(result);
  }
}
