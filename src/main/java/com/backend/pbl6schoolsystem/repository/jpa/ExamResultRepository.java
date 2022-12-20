package com.backend.pbl6schoolsystem.repository.jpa;

import com.backend.pbl6schoolsystem.model.dto.clazz.ExamResultClassDTO;
import com.backend.pbl6schoolsystem.model.entity.ExamResultEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    @Query("SELECT er FROM ExamResultEntity er" +
            " WHERE er.schoolYear.schoolYearId = :schoolYearId" +
            " AND er.semester.semesterId = :semesterId" +
            " AND er.subject.subjectId = :subjectId" +
            " AND er.student.userId = :studentId" +
            " AND er.examType = :type")
    Optional<ExamResultEntity> findFromDB(@Param("subjectId") Long subjectId, @Param("schoolYearId") Long schoolYearId,
                                                       @Param("semesterId") Long semesterId, @Param("studentId") Long studentId, @Param("type") String type);

    @Query("SELECT er FROM ExamResultEntity er" +
            " WHERE er.student.userId = :studentId")
    List<ExamResultEntity> findByStudentId(@Param("studentId") Long studentId);
}
