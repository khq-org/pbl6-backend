package com.backend.pbl6schoolsystem.repository.jpa;

import com.backend.pbl6schoolsystem.model.entity.ClassCalendarEventEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassCalendarRepository extends JpaRepository<ClassCalendarEventEntity, Long> {
    @Query("SELECT cc FROM ClassCalendarEventEntity cc" +
            " JOIN FETCH cc.calendarEvent ce" +
            " JOIN FETCH ce.room" +
            " WHERE cc.clazz.classId = :classId")
    List<ClassCalendarEventEntity> listClassCalendarEvent(@Param("classId") Long classId);
}
