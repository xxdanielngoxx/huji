package com.github.xxdanielngoxx.hui.api.shared.validation.phonenumber;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VnPhoneNumberValidator implements ConstraintValidator<VnPhoneNumber, String> {

  public static final String REGEX_PATTERN = "^(?:\\+84|0084|0)[235789]\\d{8}$";

  public static final String ERROR_MESSAGE = "must be a well-formed Vietnam phone number";

  private static final Pattern VN_PHONE_NUMBER_PATTERN = Pattern.compile(REGEX_PATTERN);

  @Override
  public void initialize(VnPhoneNumber constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (Objects.isNull(value)) {
      return true;
    }

    final Matcher matcher = VN_PHONE_NUMBER_PATTERN.matcher(value);
    final boolean isValid = matcher.matches();
    if (isValid) {
      return true;
    }

    context.disableDefaultConstraintViolation();
    context.buildConstraintViolationWithTemplate(ERROR_MESSAGE).addConstraintViolation();
    return false;
  }
}
