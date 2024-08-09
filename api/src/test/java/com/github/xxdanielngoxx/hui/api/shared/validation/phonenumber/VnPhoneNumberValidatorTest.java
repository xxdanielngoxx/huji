package com.github.xxdanielngoxx.hui.api.shared.validation.phonenumber;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class VnPhoneNumberValidatorTest {
  private static Validator validator;

  @BeforeAll
  static void setupAll() {
    try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
      validator = validatorFactory.getValidator();
    }
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = {"0393238017", "+84393238017"})
  void should_not_return_violation_when_phone_number_is_valid(final String phoneNumber) {
    final Dummy dummy = Dummy.builder().phoneNumber(phoneNumber).build();

    final Set<ConstraintViolation<Dummy>> constraintViolations = validator.validate(dummy);

    assertThat(constraintViolations).isEmpty();
  }

  @ParameterizedTest
  @ValueSource(strings = {"03932380173534534", "+84393238017234234", "+841234", "039323"})
  void should_return_violation_when_phone_number_is_valid(final String phoneNumber) {
    final Dummy dummy = Dummy.builder().phoneNumber(phoneNumber).build();

    final Set<ConstraintViolation<Dummy>> constraintViolations = validator.validate(dummy);

    assertThat(constraintViolations).hasSize(1);

    final List<String> errorMessages =
        constraintViolations.stream().map(ConstraintViolation::getMessage).toList();
    assertThat(errorMessages).contains(VnPhoneNumberValidator.ERROR_MESSAGE);
  }
}

@Getter
@Setter
@Builder
class Dummy {
  @VnPhoneNumber private String phoneNumber;
}
