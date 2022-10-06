package com.backend.pbl6schoolsystem.repository.jpa;

import com.backend.pbl6schoolsystem.model.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
}
