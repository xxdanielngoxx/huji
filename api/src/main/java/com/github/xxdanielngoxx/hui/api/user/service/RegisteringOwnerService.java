package com.github.xxdanielngoxx.hui.api.user.service;

import com.github.xxdanielngoxx.hui.api.user.service.command.RegisterOwnerCommand;
import jakarta.annotation.Nonnull;
import java.util.UUID;

public interface RegisteringOwnerService {
  UUID register(@Nonnull RegisterOwnerCommand command);
}
