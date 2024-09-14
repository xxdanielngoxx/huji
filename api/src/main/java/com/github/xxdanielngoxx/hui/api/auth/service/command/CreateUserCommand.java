package com.github.xxdanielngoxx.hui.api.auth.service.command;

import com.github.xxdanielngoxx.hui.api.auth.model.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateUserCommand {
  private String username;
  private String password;
  private String phoneNumber;
  private Role role;
}
