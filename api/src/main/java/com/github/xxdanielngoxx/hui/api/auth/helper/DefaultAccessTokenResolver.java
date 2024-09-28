package com.github.xxdanielngoxx.hui.api.auth.helper;

import static com.github.xxdanielngoxx.hui.api.auth.constant.CookieConstant.FINGERING_COOKIE_NAME;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;

public class DefaultAccessTokenResolver implements AccessTokenResolver {
  @Override
  public Optional<AccessToken> resolve(@Nonnull final HttpServletRequest request) {
    final String accessToken = resolveAccessToken(request);
    if (StringUtils.isBlank(accessToken)) {
      return Optional.empty();
    }

    final String fingering = resolveFingering(request);
    if (StringUtils.isBlank(fingering)) {
      return Optional.empty();
    }

    return Optional.of(new AccessToken(accessToken, fingering));
  }

  private String resolveAccessToken(@Nonnull final HttpServletRequest request) {
    final String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (StringUtils.isBlank(bearerToken)) {
      return StringUtils.EMPTY;
    }
    return StringUtils.substring(bearerToken, "Bearer ".length()).trim();
  }

  private String resolveFingering(@Nonnull final HttpServletRequest request) {
    final Cookie[] cookies = request.getCookies();

    return Optional.ofNullable(cookies).stream()
        .flatMap(Arrays::stream)
        .filter(cookie -> StringUtils.equals(cookie.getName(), FINGERING_COOKIE_NAME))
        .map(Cookie::getValue)
        .filter(StringUtils::isNotBlank)
        .findAny()
        .orElse(StringUtils.EMPTY);
  }
}
