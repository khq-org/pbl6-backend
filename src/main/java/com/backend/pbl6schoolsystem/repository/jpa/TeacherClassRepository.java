package com.backend.pbl6schoolsystem.repository.jpa;

import com.backend.pbl6schoolsystem.model.entity.ClassEntity;
import com.backend.pbl6schoolsystem.model.entity.TeacherClassEntity;
import com.backend.pbl6schoolsystem.model.entity.UserEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherClassRepository extends JpaRepository<TeacherClassEntity, Long> {
    @Query("SELECT tc FROM TeacherClassEntity tc" +
            " LEFT JOIN FETCH tc.clazz c" +
            " LEFT JOIN FETCH c.grade" +
            " LEFT JOIN FETCH tc.schoolYear sy" +
            " LEFT JOIN FETCH tc.semester s" +
            " WHERE tc.teacher.userId = :teacherId")
    List<TeacherClassEntity> findByTeacher(@Param("teacherId") Long teacherId);

    @Query("SELECT tc FROM TeacherClassEntity tc" +
            " WHERE tc.teacher.userId = :teacherId" +
            " AND tc.clazz.classId = :classId" +
            " AND tc.schoolYear.schoolYearId = :schoolYearId" +
            " AND tc.semester.semesterId = :semesterId")
    Optional<TeacherClassEntity> findByExceptId(@Param("teacherId") Long teacherId, @Param("classId") Long classId, @Param("semesterId") Long semesterId,
                                                @Param("schoolYearId") Long schoolYearId);

//    @Query("SELECT tc FROM TeacherClassEntity tc" +
//            " LEFT JOIN FETCH tc.schoolYear" +
//            " WHERE tc.clazz.classId = :classId" +
//            " AND tc.teacher.teacherId = :teacherId")
//    TeacherClassEntity findByTeacherAndClass(@Param("teacherId") Long teacherId, @Param("classId") Long classId);
}
