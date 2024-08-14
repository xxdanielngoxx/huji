package com.github.xxdanielngoxx.hui.api.owner.model;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@Builder
@Getter
@Setter
@Entity
@Table(name = "owner")
@NoArgsConstructor
@AllArgsConstructor
public class OwnerEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "full_name", nullable = false)
  private String fullName;

  @Column(name = "phone_number", nullable = false, unique = true)
  private String phoneNumber;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "email", nullable = false, unique = true)
  private String email;
}
