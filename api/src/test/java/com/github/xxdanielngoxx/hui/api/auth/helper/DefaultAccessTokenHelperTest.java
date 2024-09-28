package com.github.xxdanielngoxx.hui.api.auth.helper;

import static com.github.xxdanielngoxx.hui.api.auth.helper.AccessTokenHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import com.github.xxdanielngoxx.hui.api.auth.model.Role;
import io.jsonwebtoken.MalformedJwtException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class DefaultAccessTokenHelperTest {

  private static final String SIGNING_KEY =
      "v4Gf29iN0bxJekFIq5Hs3k1LI8xKkEkDTjQ0x0CCHp3R+73XKV4848xUjGogyNHugRzq/rFEDhY8dlLvDWu9lA==";

  private final FingeringHelper fingeringHelper = mock(FingeringHelper.class);

  private final DefaultAccessTokenHelper defaultAccessTokenHelper =
      new DefaultAccessTokenHelper(SIGNING_KEY, DEFAULT_EXPIRATION_IN_SECONDS, fingeringHelper);

  @Nested
  class IssueTest {
    @Test
    void should_issue_new_token() {
      final CredentialsValidationResult credentialsValidationResult =
          CredentialsValidationResult.builder()
              .principal("danielngo1998@gmail.com")
              .role(Role.OWNER)
              .build();

      final String fingering = "fingering";

      final String hashedFingering = "fingering:hashed";
      given(fingeringHelper.hash(fingering)).willReturn(hashedFingering);

      final String issuedToken =
          defaultAccessTokenHelper.issue(credentialsValidationResult, fingering);

      final String[] components = issuedToken.split("\\.");

      final String header = components[0];

      final String decodedHeader =
          new String(
              Base64.getDecoder().decode(header.getBytes(StandardCharsets.UTF_8)),
              StandardCharsets.UTF_8);
      assertThat(decodedHeader).startsWith("{\"alg\":\"HS");

      final String payload = components[1];
      final String decodedPayload =
          new String(
              Base64.getDecoder().decode(payload.getBytes(StandardCharsets.UTF_8)),
              StandardCharsets.UTF_8);
      assertThat(decodedPayload).contains("\"sub\":\"danielngo1998@gmail.com\"");
      assertThat(decodedPayload).contains("\"role\":\"OWNER\"");
      assertThat(decodedPayload).contains("\"iss\":\"huji-api\"");
      assertThat(decodedPayload)
          .contains(String.format("\"%s\":\"%s\"", FINGERING_CLAIM_NAME, hashedFingering));

      then(fingeringHelper).should(times(1)).hash(fingering);
    }
  }

  @Nested
  class VerifyTest {

    @Test
    void should_return_claims_when_token_is_valid() {
      final CredentialsValidationResult credentialsValidationResult =
          CredentialsValidationResult.builder()
              .principal("danielngo1998@gmail.com")
              .role(Role.OWNER)
              .build();

      final String fingering = "fingering";

      given(fingeringHelper.matches(eq(fingering), any())).willReturn(true);

      final String issuedToken =
          defaultAccessTokenHelper.issue(credentialsValidationResult, fingering);

      final AccessTokenAuthenticatedPrincipal principal =
          defaultAccessTokenHelper.verify(issuedToken, fingering);

      final String subject = principal.subject();
      assertThat(subject).isEqualTo(credentialsValidationResult.principal());

      final Role role = principal.role();
      assertThat(role).isEqualTo(credentialsValidationResult.role());

      then(fingeringHelper).should(times(1)).matches(eq(fingering), any());
    }

    @Test
    void should_throw_when_fingering_is_not_match() {
      final CredentialsValidationResult credentialsValidationResult =
          CredentialsValidationResult.builder()
              .principal("danielngo1998@gmail.com")
              .role(Role.OWNER)
              .build();

      final String fingering = "fingering";

      given(fingeringHelper.matches(eq(fingering), any())).willReturn(false);

      final String issuedToken =
          defaultAccessTokenHelper.issue(credentialsValidationResult, fingering);

      final MalformedJwtException exception =
          assertThrows(
              MalformedJwtException.class,
              () -> defaultAccessTokenHelper.verify(issuedToken, fingering));

      assertThat(exception.getMessage()).contains("invalid access token");

      then(fingeringHelper).should(times(1)).matches(eq(fingering), any());
    }

    @Test
    void should_throw_when_token_is_invalid() {
      assertThrows(
          Exception.class,
          () -> defaultAccessTokenHelper.verify("<<invalid_token>>", "<<fingering>>"));
    }
  }
}
