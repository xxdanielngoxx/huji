package com.github.xxdanielngoxx.hui.api.shared.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration(proxyBeanMethods = false)
public class PasswordEncoderConfig {

  @Bean
  public PasswordEncoder defaultPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
