package com.github.xxdanielngoxx.hui.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

/*
 * TODO: Revert when setup CD is done
 * @Import({PostgresTestcontainerConfiguration.class})
 */
@Profile("test") // TODO: Remove when setup CD is done
@SpringBootTest
class ApiApplicationTests {

  @Test
  void contextLoads() {
    // Load the context to ensure the application can start without any errors
  }
}
