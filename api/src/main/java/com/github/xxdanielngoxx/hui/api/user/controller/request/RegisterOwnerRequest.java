package com.github.xxdanielngoxx.hui.api.user.controller.request;

import com.github.xxdanielngoxx.hui.api.shared.validation.phonenumber.VnPhoneNumber;
import com.github.xxdanielngoxx.hui.api.shared.validation.phonenumber.VnPhoneNumberValidator;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class RegisterOwnerRequest {

  @NotNull private String fullName;

  @Email private String email;

  @Schema(
      description = "Must be a well-formed Vietnam phone number",
      pattern = VnPhoneNumberValidator.REGEX_PATTERN,
      examples = {"0393238017", "+84393238017"})
  @NotNull
  @VnPhoneNumber
  private String phoneNumber;

  @NotNull
  @Size(min = 8, max = 64)
  private String password;
}
