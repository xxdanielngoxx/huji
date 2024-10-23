package com.github.xxdanielngoxx.hui.api.auth.controller.response;

import com.github.xxdanielngoxx.hui.api.auth.model.Role;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class UserResource {
  private String id;
  private String username;
  private String phoneNumber;
  private Role role;
}
