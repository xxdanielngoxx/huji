package com.github.xxdanielngoxx.hui.api.auth.helper;

import com.github.xxdanielngoxx.hui.api.auth.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Nonnull;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.util.*;
import javax.crypto.SecretKey;

public class DefaultAccessTokenHelper implements AccessTokenHelper {

  private final SecretKey signingKey;

  private final long expirationInSeconds;

  private final FingeringHelper fingeringHelper;

  public DefaultAccessTokenHelper(
      final String signingKey,
      final Long expirationInSeconds,
      final FingeringHelper fingeringHelper) {
    this.signingKey = Keys.hmacShaKeyFor(signingKey.getBytes(StandardCharsets.UTF_8));
    this.expirationInSeconds =
        Objects.isNull(expirationInSeconds) ? DEFAULT_EXPIRATION_IN_SECONDS : expirationInSeconds;

    this.fingeringHelper = fingeringHelper;
  }

  @Override
  public String issue(
      @Nonnull final CredentialsValidationResult credentialsValidationResult,
      @Nonnull final String fingering) {

    final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("UTC")));
    final Date now = calendar.getTime();
    final Date expiration = new Date(now.getTime() + expirationInSeconds * 1000);

    final String hashedFingering = fingeringHelper.hash(fingering);

    return Jwts.builder()
        .signWith(signingKey)
        .id(UUID.randomUUID().toString())
        .issuer(ISSUER)
        .subject(credentialsValidationResult.principal())
        .claim(ROLE_CLAIM_NAME, credentialsValidationResult.role().name())
        .claim(FINGERING_CLAIM_NAME, hashedFingering)
        .issuedAt(now)
        .expiration(expiration)
        .compact();
  }

  @Override
  public AccessTokenAuthenticatedPrincipal verify(
      @Nonnull final String token, @Nonnull final String fingering) {
    final Jws<Claims> claims =
        Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token);

    final String hashedFingering = claims.getPayload().get(FINGERING_CLAIM_NAME, String.class);

    if (!fingeringHelper.matches(fingering, hashedFingering)) {
      throw new MalformedJwtException("invalid access token");
    }

    return new AccessTokenAuthenticatedPrincipal(
        claims.getPayload().getSubject(), extractRole(claims.getPayload()));
  }

  private Role extractRole(@Nonnull Claims claims) {
    return Role.valueOf(claims.get(ROLE_CLAIM_NAME, String.class));
  }
}
