package com.github.xxdanielngoxx.hui.api.auth.service;

import com.github.xxdanielngoxx.hui.api.auth.service.command.CreateUserCommand;
import java.util.UUID;

public interface CreateUserService {
  UUID createUser(CreateUserCommand command);
}
