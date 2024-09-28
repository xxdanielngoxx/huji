package com.github.xxdanielngoxx.hui.api.auth.repository;

import com.github.xxdanielngoxx.hui.api.auth.model.UserEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
  boolean existsByUsername(String username);

  boolean existsByPhoneNumber(String phoneNumber);

  Optional<UserEntity> findByUsername(String username);

  @Query("select u from UserEntity u where u.username = ?1 or u.phoneNumber = ?1")
  Optional<UserEntity> findByUsernameOrPhoneNumber(String value);
}
