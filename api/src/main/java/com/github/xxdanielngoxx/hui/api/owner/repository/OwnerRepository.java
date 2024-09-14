package com.github.xxdanielngoxx.hui.api.owner.repository;

import com.github.xxdanielngoxx.hui.api.owner.model.OwnerEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepository extends JpaRepository<OwnerEntity, UUID> {}
