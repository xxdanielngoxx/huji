package com.github.xxdanielngoxx.hui.api.auth.helper;

import jakarta.annotation.Nonnull;
import jakarta.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class DefaultFingeringHelper implements FingeringHelper {

  public static final String DIGEST_ALGORITHM = "SHA-256";

  @Override
  public String generate() {
    final byte[] fingering = new byte[FINGERING_SIZE_IN_BYTES];

    final SecureRandom secureRandom = new SecureRandom();
    secureRandom.nextBytes(fingering);

    return DatatypeConverter.printHexBinary(fingering);
  }

  @SneakyThrows
  @Override
  public String hash(@Nonnull final String fingering) {
    final MessageDigest messageDigest = MessageDigest.getInstance(DIGEST_ALGORITHM);

    final byte[] digestedFingering =
        messageDigest.digest(fingering.getBytes(StandardCharsets.UTF_8));

    return DatatypeConverter.printHexBinary(digestedFingering);
  }

  @Override
  public boolean matches(final String fingering, final String hashedFingering) {
    if (StringUtils.isBlank(fingering)) {
      return false;
    }

    if (StringUtils.isBlank(hashedFingering)) {
      return false;
    }

    return StringUtils.equals(hash(fingering), hashedFingering);
  }
}
