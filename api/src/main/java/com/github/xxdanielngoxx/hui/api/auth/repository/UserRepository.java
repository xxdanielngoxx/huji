package com.github.xxdanielngoxx.hui.api.auth.repository;

import com.github.xxdanielngoxx.hui.api.auth.model.UserEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
  boolean existsByUsername(String username);

  boolean existsByPhoneNumber(String phoneNumber);
}
