package com.backend.pbl6schoolsystem.repository.jpa;

import com.backend.pbl6schoolsystem.model.entity.LearningResultEntity;
import com.backend.pbl6schoolsystem.model.entity.ProfileStudentEntity;
import com.backend.pbl6schoolsystem.request.leaningresult.CreateUpdateLearningResultRequest;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LearningResultRepository extends JpaRepository<LearningResultEntity, Long> {
    @Query("SELECT lr FROM LearningResultEntity lr" +
            " LEFT JOIN FETCH lr.clazz" +
            " LEFT JOIN FETCH lr.schoolYear" +
            " WHERE lr.profileStudent.profileStudentId = :profileStudentId")
    List<LearningResultEntity> findByProfileStudent(@Param("profileStudentId") Long profileStudentId);

    @Query("SELECT lr FROM LearningResultEntity lr" +
            " LEFT JOIN FETCH lr.profileStudent ps" +
            " LEFT JOIN FETCH ps.student" +
            " LEFT JOIN FETCH lr.clazz" +
            " WHERE lr.learningResultId = :learningResultId")
    Optional<LearningResultEntity> findByLearningResult(@Param("learningResultId") Long learningResultId);

    @Query("SELECT lr FROM LearningResultEntity lr" +
            " WHERE lr.profileStudent.profileStudentId = :profileStudentId" +
            " AND lr.clazz.classId = :classId" +
            " AND lr.schoolYear.schoolYearId = :schoolYearId" +
            " AND :learningResultId IS NULL OR (:learningResultId IS NOT NULL AND lr.learningResultId <> :learningResultId)")
    Optional<LearningResultEntity> findByProfileAndClazzAndSchoolYear(@Param("profileStudentId") Long profileStudentId,
                                                                      @Param("learningResultId") Long learningResultId,
                                                                      @Param("classId") Long classId,
                                                                      @Param("schoolYearId") Long schoolYearId);
}
