package com.backend.pbl6schoolsystem.repository.jpa;

import com.backend.pbl6schoolsystem.model.entity.ClassEntity;
import com.backend.pbl6schoolsystem.model.entity.StudentClazzEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentClazzRepository extends JpaRepository<StudentClazzEntity, Long> {
    @Query("SELECT c FROM StudentClazzEntity sc" +
            " INNER JOIN sc.clazz c" +
            " ON sc.clazz.classId = c.classId" +
            " WHERE sc.student.userId = :studentId" +
            " ORDER BY sc.studentClassId DESC")
    List<ClassEntity> getCurrentClassForStudent(@Param("studentId") Long studentId);


    @Query("SELECT sc FROM StudentClazzEntity sc" +
            " LEFT JOIN FETCH sc.clazz" +
            " LEFT JOIN FETCH sc.student s" +
            " WHERE s.userId IN (:studentIds)" +
            " ORDER BY sc.studentClassId DESC")
    List<StudentClazzEntity> findByStudents(@Param("studentIds") List<Long> studentIds);
}
