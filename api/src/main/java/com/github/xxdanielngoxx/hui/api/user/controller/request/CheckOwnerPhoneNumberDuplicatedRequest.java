package com.github.xxdanielngoxx.hui.api.user.controller.request;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class CheckOwnerPhoneNumberDuplicatedRequest {
  private String phoneNumber;
}
