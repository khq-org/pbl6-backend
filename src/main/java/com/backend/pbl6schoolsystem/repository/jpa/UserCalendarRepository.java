package com.backend.pbl6schoolsystem.repository.jpa;

import com.backend.pbl6schoolsystem.model.entity.UserCalendarEventEntity;
import com.backend.pbl6schoolsystem.model.entity.UserEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCalendarRepository extends JpaRepository<UserCalendarEventEntity, Long> {
    @Query("SELECT uc FROM UserCalendarEventEntity uc" +
            " LEFT JOIN FETCH uc.calendarEvent ce" +
            " LEFT JOIN FETCH ce.room" +
            " LEFT JOIN FETCH ce.subject" +
            " WHERE uc.user.userId = :userId")
    List<UserCalendarEventEntity> findListUserCalendar(@Param("userId") Long userId);

    @Query("SELECT uce FROM UserCalendarEventEntity uce" +
            " LEFT JOIN FETCH uce.user" +
            " WHERE uce.calendarEvent.calendarEventId = :calendarEventId")
    List<UserCalendarEventEntity> findByCalendar(@Param("calendarEventId") Long calendarEventId);
}
