package com.github.xxdanielngoxx.hui.api.auth.model;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "user")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "username", unique = true)
  private String username;

  @Column(name = "password")
  private String password;

  @Column(name = "phone_number", unique = true)
  private String phoneNumber;

  @Column(name = "role", nullable = false, updatable = false)
  @Enumerated(EnumType.STRING)
  private Role role;
}
