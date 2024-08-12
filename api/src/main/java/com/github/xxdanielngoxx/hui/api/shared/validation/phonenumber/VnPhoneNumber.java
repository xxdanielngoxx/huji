package com.github.xxdanielngoxx.hui.api.shared.validation.phonenumber;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = VnPhoneNumberValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD, ElementType.PARAMETER})
public @interface VnPhoneNumber {
  String message() default
      "{com.github.xxdanielngoxx.hui.api.shared.validation.phonenumber.VnPhoneNumber}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
