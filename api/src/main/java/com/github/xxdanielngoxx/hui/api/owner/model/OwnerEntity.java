package com.github.xxdanielngoxx.hui.api.owner.model;

import com.github.xxdanielngoxx.hui.api.auth.model.UserEntity;
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

  @Id private UUID id;

  @Column(name = "full_name", nullable = false)
  private String fullName;

  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  @JoinColumn(name = "id")
  private UserEntity user;
}
