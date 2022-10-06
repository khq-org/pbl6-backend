package com.backend.pbl6schoolsystem.repository.jpa;

import com.backend.pbl6schoolsystem.model.entity.UserEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query("SELECT s FROM UserEntity s" +
            " LEFT JOIN FETCH s.school sch" +
            " WHERE s.role.roleId = :roleId" +
            " AND s.userId = :studentId")
    Optional<UserEntity> findOneById(@Param("studentId") Long studentId, @Param("roleId") Long roleId);
}
