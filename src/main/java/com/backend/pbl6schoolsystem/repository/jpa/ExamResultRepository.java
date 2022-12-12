package com.backend.pbl6schoolsystem.repository.jpa;

import com.backend.pbl6schoolsystem.model.entity.ExamResultEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamResultRepository extends JpaRepository<ExamResultEntity, Long> {
    @Query("SELECT er FROM ExamResultEntity er" +
            " LEFT JOIN FETCH er.learningResult lr" +
            " LEFT JOIN FETCH er.subject s" +
            " LEFT JOIN FETCH er.semester" +
            " WHERE lr.learningResultId = :learningResultId" +
            " AND s.subjectId IN (:subjectIds)")
    List<ExamResultEntity> listExamResult(@Param("learningResultId") Long learningResultId,
                                          @Param("subjectIds") List<Long> subjectIds);

}
