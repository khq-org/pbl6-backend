package com.backend.pbl6schoolsystem.repository.jpa;

import com.backend.pbl6schoolsystem.model.entity.LearningResultEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LearningResultRepository extends JpaRepository<LearningResultEntity, Long> {
    @Query("SELECT lr FROM LearningResultEntity lr" +
            " LEFT JOIN FETCH lr.clazz" +
            " LEFT JOIN FETCH lr.schoolYear" +
            " WHERE lr.profileStudent.profileStudentId = :profileStudentId")
    List<LearningResultEntity> findByProfileStudent(@Param("profileStudentId") Long profileStudentId);
}
