package com.github.xxdanielngoxx.hui.api.owner.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.github.xxdanielngoxx.hui.api.PostgresTestcontainerConfiguration;
import com.github.xxdanielngoxx.hui.api.auth.model.Role;
import com.github.xxdanielngoxx.hui.api.auth.model.UserEntity;
import com.github.xxdanielngoxx.hui.api.auth.repository.UserRepository;
import com.github.xxdanielngoxx.hui.api.owner.model.OwnerEntity;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Import({PostgresTestcontainerConfiguration.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OwnerRepositoryTest {

  @Autowired private OwnerRepository ownerRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private JdbcTemplate jdbcTemplate;

  @BeforeEach
  void setup() {
    cleanupDatabase();
  }

  void cleanupDatabase() {
    JdbcTestUtils.deleteFromTables(jdbcTemplate, "\"user\"", "owner");
  }

  @Nested
  class SaveTest {
    @Test
    @Transactional
    void should_persist_success() {
      final UserEntity user =
          userRepository.save(
              UserEntity.builder().username("danielngo1998@gmail.com").role(Role.OWNER).build());

      final OwnerEntity owner = OwnerEntity.builder().fullName("Ngô Đình Lộc").user(user).build();

      final UUID ownerId = ownerRepository.save(owner).getId();

      final OwnerEntity savedOwner = ownerRepository.findById(ownerId).orElseThrow();

      assertThat(savedOwner.getFullName()).isEqualTo(owner.getFullName());
    }
  }
}
