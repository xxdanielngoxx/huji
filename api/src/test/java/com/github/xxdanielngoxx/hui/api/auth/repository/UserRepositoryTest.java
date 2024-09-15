package com.github.xxdanielngoxx.hui.api.auth.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.github.xxdanielngoxx.hui.api.PostgresTestcontainerConfiguration;
import com.github.xxdanielngoxx.hui.api.auth.model.Role;
import com.github.xxdanielngoxx.hui.api.auth.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
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
class UserRepositoryTest {
  @Autowired private UserRepository userRepository;

  @Autowired private JdbcTemplate jdbcTemplate;

  @BeforeEach
  void setup() {
    this.cleanupDatabase();
  }

  private void cleanupDatabase() {
    JdbcTestUtils.deleteFromTables(jdbcTemplate, "\"user\"");
  }

  @Nested
  class SaveTest {
    @Test
    void should_persist_user() {
      final UserEntity user =
          UserEntity.builder()
              .username("danielngo1998@gmail.com")
              .phoneNumber("0393238017")
              .password("<<redacted>>")
              .role(Role.OWNER)
              .build();

      userRepository.save(user);

      final UserEntity savedUser = userRepository.findById(user.getId()).orElseThrow();

      assertThat(user.getUsername()).isEqualTo(savedUser.getUsername());
      assertThat(user.getPhoneNumber()).isEqualTo(savedUser.getPhoneNumber());
      assertThat(user.getPassword()).isEqualTo(savedUser.getPassword());
      assertThat(user.getRole()).isEqualTo(savedUser.getRole());
    }

    @Test
    void given_user_has_undefined_role_when_save_then_fail() {
      final UserEntity user =
          UserEntity.builder()
              .username("danielngo1998@gmail.com")
              .phoneNumber("0393238017")
              .build();

      final DataIntegrityViolationException exception =
          assertThrows(
              DataIntegrityViolationException.class,
              () -> {
                userRepository.save(user);
              });

      assertThat(exception.getMessage())
          .contains(
              "null value in column \"role\" of relation \"user\" violates not-null constraint");
    }

    @Test
    void should_throw_exception_when_username_is_not_unique() {
      final String username = "danielngo1998@gmail.com";

      final UserEntity user1 = UserEntity.builder().username(username).role(Role.OWNER).build();

      userRepository.save(user1);

      final UserEntity user2 = UserEntity.builder().username(username).role(Role.OWNER).build();

      final DataIntegrityViolationException exception =
          assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(user2));

      assertThat(exception.getMessage())
          .contains("(username)=(danielngo1998@gmail.com) already exists");
    }

    @Test
    void should_throw_exception_when_phone_number_is_not_unique() {
      final String phoneNumber = "0393238017";

      final UserEntity user1 =
          UserEntity.builder().phoneNumber(phoneNumber).role(Role.OWNER).build();

      userRepository.save(user1);

      final UserEntity user2 =
          UserEntity.builder().phoneNumber(phoneNumber).role(Role.OWNER).build();

      final DataIntegrityViolationException exception =
          assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(user2));

      assertThat(exception.getMessage()).contains("(phone_number)=(0393238017) already exists");
    }
  }

  @Nested
  class ExistsByPhoneNumberTest {

    @Test
    void should_return_true_when_phone_number_was_used_by_an_user() {
      final String phoneNumber = "0393238017";

      final UserEntity userEntity =
          UserEntity.builder()
              .phoneNumber("0393238017")
              .password("<<redacted>>")
              .username("danielngo1998@gmail.com")
              .role(Role.OWNER)
              .build();

      userRepository.save(userEntity);

      final boolean result = userRepository.existsByPhoneNumber(phoneNumber);
      assertThat(result).isTrue();
    }

    @Test
    void should_return_false_when_phone_number_is_not_used_by_any_user() {
      final String phoneNumber = "0393238017";

      final boolean result = userRepository.existsByPhoneNumber(phoneNumber);

      assertThat(result).isFalse();
    }
  }

  @Nested
  class ExistsByUsernameTest {

    @Test
    void should_return_true_when_username_was_already_used_by_an_user() {
      final String username = "danielngo1998@gmail.com";

      final UserEntity userEntity =
          UserEntity.builder()
              .phoneNumber("0393238017")
              .password("<<redacted>>")
              .username(username)
              .role(Role.OWNER)
              .build();

      userRepository.save(userEntity);

      final boolean result = userRepository.existsByUsername(username);
      assertThat(result).isTrue();
    }

    @Test
    void should_return_false_when_username_is_not_used_by_any_username() {
      final String username = "danielngo1998@gmail.com";

      final boolean result = userRepository.existsByUsername(username);

      assertThat(result).isFalse();
    }
  }
}
