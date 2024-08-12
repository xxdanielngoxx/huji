package com.github.xxdanielngoxx.hui.api.user.repository;

import com.github.xxdanielngoxx.hui.api.user.model.OwnerEntity;
import jakarta.annotation.Nonnull;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepository extends JpaRepository<OwnerEntity, UUID> {
  boolean existsByPhoneNumber(@Nonnull final String phoneNumber);
}
