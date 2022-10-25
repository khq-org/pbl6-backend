package com.backend.pbl6schoolsystem.repository.jpa;

import com.backend.pbl6schoolsystem.model.entity.ClassEntity;
import com.backend.pbl6schoolsystem.model.entity.StudentClazzEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentClazzRepository extends JpaRepository<StudentClazzEntity, Long> {
    @Query("SELECT c FROM StudentClazzEntity sc" +
            " INNER JOIN sc.clazz c" +
            " WHERE sc.student.userId = :studentId" +
            " ORDER BY sc.studentClassId DESC")
    Optional<ClassEntity> getCurrentClassForStudent(@Param("studentId") Long studentId);
}
