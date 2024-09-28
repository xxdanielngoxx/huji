package com.github.xxdanielngoxx.hui.api.auth.helper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.github.xxdanielngoxx.hui.api.auth.model.Role;
import org.junit.jupiter.api.Test;

class AccessTokenAuthenticatedPrincipalTest {

  @Test
  void should_create_with_expected_properties() {
    final AccessTokenAuthenticatedPrincipal principal =
        new AccessTokenAuthenticatedPrincipal("danielngo1998@gmail.com", Role.OWNER);

    assertThat(principal.getName()).isEqualTo("danielngo1998@gmail.com");
    assertThat(principal.subject()).isEqualTo("danielngo1998@gmail.com");
    assertThat(principal.role()).isEqualTo(Role.OWNER);
  }
}
