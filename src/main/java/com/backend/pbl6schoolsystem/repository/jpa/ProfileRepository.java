package com.backend.pbl6schoolsystem.repository.jpa;

import com.backend.pbl6schoolsystem.model.entity.ProfileStudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<ProfileStudentEntity, Long> {
}
