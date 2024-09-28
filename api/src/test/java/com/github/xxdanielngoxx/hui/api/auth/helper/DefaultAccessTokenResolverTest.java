package com.github.xxdanielngoxx.hui.api.auth.helper;

import static com.github.xxdanielngoxx.hui.api.auth.constant.CookieConstant.FINGERING_COOKIE_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.servlet.http.Cookie;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;

class DefaultAccessTokenResolverTest {

  private final AccessTokenResolver accessTokenResolver = new DefaultAccessTokenResolver();

  @Test
  void should_return_empty_when_bearer_token_is_empty() {
    final MockHttpServletRequest mockRequest = new MockHttpServletRequest();

    final String fingering = "fingering";
    mockRequest.setCookies(new Cookie(FINGERING_COOKIE_NAME, fingering));

    final Optional<AccessToken> optionalAccessToken = accessTokenResolver.resolve(mockRequest);

    assertThat(optionalAccessToken).isEmpty();
  }

  @Test
  void should_return_empty_when_fingering_is_empty() {
    final MockHttpServletRequest mockRequest = new MockHttpServletRequest();
    mockRequest.addHeader(HttpHeaders.AUTHORIZATION, "Bearer access_token");

    final Optional<AccessToken> optionalAccessToken = accessTokenResolver.resolve(mockRequest);

    assertThat(optionalAccessToken).isEmpty();
  }

  @Test
  void should_return_access_token_when_bearer_token_and_fingering_are_present() {
    final MockHttpServletRequest mockRequest = new MockHttpServletRequest();

    final String bearerToken = "Bearer access_token";
    mockRequest.addHeader(HttpHeaders.AUTHORIZATION, bearerToken);

    final String fingering = "fingering";
    mockRequest.setCookies(new Cookie(FINGERING_COOKIE_NAME, fingering));

    final Optional<AccessToken> optionalAccessToken = accessTokenResolver.resolve(mockRequest);

    assertThat(optionalAccessToken).isNotEmpty();
    assertThat(optionalAccessToken.get()).isEqualTo(new AccessToken("access_token", fingering));
  }
}
