package com.github.xxdanielngoxx.hui.api.auth.helper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.github.xxdanielngoxx.hui.api.auth.model.Role;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

@ExtendWith(MockitoExtension.class)
class AccessTokenAuthenticationProviderTest {

  @Mock AccessTokenHelper accessTokenHelper;

  AccessTokenAuthenticationProvider provider;

  @BeforeEach
  void beforeEach() {
    provider = new AccessTokenAuthenticationProvider(accessTokenHelper);
  }

  @Nested
  class AuthenticateTest {

    @Test
    void
        should_verify_and_return_authentication_with_granted_authorities_when_access_token_is_valid() {
      final AccessTokenAuthenticationToken bearerToken =
          new AccessTokenAuthenticationToken("token", "details", "fingering");

      final AccessTokenAuthenticatedPrincipal mockPrincipal =
          new AccessTokenAuthenticatedPrincipal("danielngo1998@gmail.com", Role.OWNER);
      given(accessTokenHelper.verify(bearerToken.getToken(), bearerToken.getFingering()))
          .willReturn(mockPrincipal);

      final Authentication result = provider.authenticate(bearerToken);

      assertThat(result.isAuthenticated()).isTrue();

      final AccessTokenAuthenticatedPrincipal principal =
          (AccessTokenAuthenticatedPrincipal) result.getPrincipal();
      assertThat(principal.getName()).isEqualTo(mockPrincipal.getName());
      assertThat(principal.role()).isEqualTo(mockPrincipal.role());

      assertThat(result.getDetails()).isEqualTo(bearerToken.getDetails());
      assertThat(result.getCredentials()).isNull();

      final Collection<? extends GrantedAuthority> grantedAuthorities = result.getAuthorities();
      assertThat(grantedAuthorities.stream().map(GrantedAuthority::getAuthority).toList())
          .hasSameElementsAs(List.of(mockPrincipal.role().name()));
      assertThat(result.getAuthorities()).isUnmodifiable();

      then(accessTokenHelper)
          .should(times(1))
          .verify((String) bearerToken.getPrincipal(), (String) bearerToken.getCredentials());
    }
  }

  @Nested
  class SupportsTest {
    @Test
    void should_return_true_when_authentication_is_a_bearer_token() {
      assertThat(provider.supports(AccessTokenAuthenticationToken.class)).isTrue();
    }
  }
}
