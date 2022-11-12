package com.backend.pbl6schoolsystem.repository.jpa;

import com.backend.pbl6schoolsystem.model.entity.ProfileStudentEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileStudentRepository extends JpaRepository<ProfileStudentEntity, Long> {
    @Query("SELECT ps FROM ProfileStudentEntity ps" +
            " LEFT JOIN FETCH ps.student s" +
            " WHERE s.userId = :studentId")
    Optional<ProfileStudentEntity> findByStudent(@Param("studentId") Long studentId);
}
