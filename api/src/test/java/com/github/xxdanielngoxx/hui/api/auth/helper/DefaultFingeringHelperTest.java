package com.github.xxdanielngoxx.hui.api.auth.helper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DefaultFingeringHelperTest {

  private final DefaultFingeringHelper defaultFingeringHelper = new DefaultFingeringHelper();

  @Nested
  class GenerateTest {
    @Test
    void should_generate_fingering() {
      final String fingering1 = defaultFingeringHelper.generate();
      assertThat(fingering1).isNotEmpty();

      final String fingering2 = defaultFingeringHelper.generate();
      assertThat(fingering2).isNotEmpty();

      assertThat(fingering1).isNotEqualTo(fingering2);
    }
  }

  @Nested
  class HashTest {
    @Test
    void should_hash_fingering() {
      final String fingering = "<<fingering>>";

      final String hashedFingering1 = defaultFingeringHelper.hash(fingering);
      assertThat(hashedFingering1).isNotEmpty();

      final String hashedFingering2 = defaultFingeringHelper.hash(fingering);
      assertThat(hashedFingering2).isNotEmpty();

      assertThat(hashedFingering1).isEqualTo(hashedFingering2);
    }
  }

  @Nested
  class MatchesTest {

    @Test
    void should_return_true_when_fingering_and_hashed_fingering_are_equal() {
      final String fingering = "<<fingering>>";
      final String hashedFingering = defaultFingeringHelper.hash(fingering);

      final boolean result = defaultFingeringHelper.matches(fingering, hashedFingering);

      assertThat(result).isTrue();
    }

    @Test
    void should_return_false_when_fingering_and_hashed_fingering_are_equal() {
      final String fingering = "<<fingering>>";
      final String hashedFingering = "<<fingering>>:<<hashed>>";

      final boolean result = defaultFingeringHelper.matches(fingering, hashedFingering);
      assertThat(result).isFalse();
    }

    @Test
    void should_return_false_when_fingering_or_hashed_fingering_is_null() {
      assertThat(defaultFingeringHelper.matches(null, "XXXXX")).isFalse();
      assertThat(defaultFingeringHelper.matches("XXXXX", null)).isFalse();
      assertThat(defaultFingeringHelper.matches(null, null)).isFalse();
    }
  }
}
