package com.github.xxdanielngoxx.hui.api.owner.service.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegisterOwnerCommand {
  private String fullName;
  private String email;
  private String phoneNumber;
  private String password;
}
