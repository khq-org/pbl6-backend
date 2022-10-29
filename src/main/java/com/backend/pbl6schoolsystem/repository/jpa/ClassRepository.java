package com.backend.pbl6schoolsystem.repository.jpa;

import com.backend.pbl6schoolsystem.model.entity.ClassEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassRepository extends JpaRepository<ClassEntity, Long> {
    @Query("SELECT sc.clazz FROM StudentClazzEntity sc" +
            " WHERE sc.student.userId = :studentId")
    List<ClassEntity> findClassesByStudent(Long studentId);

    @Query("SELECT c FROM ClassEntity c" +
            " WHERE c.school.schoolId = :schoolId" +
            " AND c.clazz = :className")
    Optional<ClassEntity> findClassByName(@Param("className") String className, @Param("schoolId") Long schoolId);

    @Query("SELECT c FROM ClassEntity c" +
            " WHERE c.classId IN (:classIds)")
    List<ClassEntity> findClassesByIds(@Param("classIds") List<Long> classIds);
}
