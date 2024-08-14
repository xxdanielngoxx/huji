package com.github.xxdanielngoxx.hui.api.owner.service;

import com.github.xxdanielngoxx.hui.api.owner.service.command.RegisterOwnerCommand;
import jakarta.annotation.Nonnull;
import java.util.UUID;

public interface RegisteringOwnerService {
  UUID register(@Nonnull RegisterOwnerCommand command);
}
