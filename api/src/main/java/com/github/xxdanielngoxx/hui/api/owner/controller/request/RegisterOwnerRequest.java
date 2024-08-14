package com.github.xxdanielngoxx.hui.api.owner.controller.request;

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

  @NotBlank private String fullName;

  @NotBlank @Email private String email;

  @Schema(
      description = "Must be a well-formed Vietnam phone number",
      pattern = VnPhoneNumberValidator.REGEX_PATTERN,
      examples = {"0393238017", "+84393238017"})
  @NotBlank
  @VnPhoneNumber
  private String phoneNumber;

  @NotBlank
  @Size(min = 8, max = 64)
  private String password;
}
