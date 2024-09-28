package com.github.xxdanielngoxx.hui.api.auth.helper;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.xxdanielngoxx.hui.api.auth.model.Role;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

class AccessTokenAuthenticatedTokenTest {
  @Test
  void should_create_with_expected_properties() {
    final AccessTokenAuthenticatedPrincipal principal =
        new AccessTokenAuthenticatedPrincipal("danielngo1998@gmail.com", Role.OWNER);

    final Object details = new Object();

    final AccessTokenAuthenticatedToken authenticatedToken =
        new AccessTokenAuthenticatedToken(principal, details);

    assertThat(authenticatedToken.isAuthenticated()).isTrue();
    assertThat(authenticatedToken.getPrincipal()).isEqualTo(principal);
    assertThat(authenticatedToken.getCredentials()).isNull();
    assertThat(authenticatedToken.getDetails()).isEqualTo(details);

    final Collection<GrantedAuthority> expectedGrantedAuthorities =
        List.of(new SimpleGrantedAuthority(principal.role().name()));
    assertThat(authenticatedToken.getAuthorities()).hasSameElementsAs(expectedGrantedAuthorities);
  }
}
