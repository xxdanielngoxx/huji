package com.github.xxdanielngoxx.hui.api.user.controller.response;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class CheckOwnerEmailDuplicatedResponse {
  private boolean duplicated;
}
