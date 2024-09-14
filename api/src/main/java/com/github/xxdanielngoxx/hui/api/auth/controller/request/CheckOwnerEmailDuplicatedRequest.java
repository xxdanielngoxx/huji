package com.github.xxdanielngoxx.hui.api.auth.controller.request;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class CheckOwnerEmailDuplicatedRequest {
  private String username;
}
