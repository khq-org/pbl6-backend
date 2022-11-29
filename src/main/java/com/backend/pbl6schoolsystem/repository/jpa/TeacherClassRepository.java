package com.backend.pbl6schoolsystem.repository.jpa;

import com.backend.pbl6schoolsystem.model.entity.TeacherClassEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherClassRepository extends JpaRepository<TeacherClassEntity, Long> {
    @Query("SELECT tc FROM TeacherClassEntity tc" +
            " LEFT JOIN FETCH tc.clazz c" +
            " LEFT JOIN FETCH tc.schoolYear sy" +
            " LEFT JOIN FETCH tc.semester s" +
            " WHERE tc.teacher.userId = :teacherId")
    List<TeacherClassEntity> findByTeacher(@Param("teacherId") Long teacherId);

}
