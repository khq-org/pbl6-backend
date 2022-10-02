package com.backend.pbl6schoolsystem.repository.jpa;

import com.backend.pbl6schoolsystem.model.entity.SchoolEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchoolRepository extends JpaRepository<SchoolEntity, Long> {
    @Query("SELECT s FROM SchoolEntity s" +
            " WHERE s.school = :name" +
            " AND s.city = :city")
    Optional<SchoolEntity> findSchoolByNameAndCity(@Param("name") String name, @Param("city") String city);
}
