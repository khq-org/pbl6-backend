package com.backend.pbl6schoolsystem.repository.jpa;

import com.backend.pbl6schoolsystem.model.entity.ParentStudentEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParentStudentRepository extends JpaRepository<ParentStudentEntity, Long> {
    @Query("SELECT ps FROM ParentStudentEntity ps" +
            " LEFT JOIN FETCH ps.parent p" +
            " WHERE ps.student.userId = :studentId")
    List<ParentStudentEntity> findParentStudentEntityByStudent(@Param("studentId") Long studentId);
}
