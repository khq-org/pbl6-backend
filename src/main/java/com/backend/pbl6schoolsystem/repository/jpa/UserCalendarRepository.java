package com.backend.pbl6schoolsystem.repository.jpa;

import com.backend.pbl6schoolsystem.model.entity.UserCalendarEventEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCalendarRepository extends JpaRepository<UserCalendarEventEntity, Long> {
    @Query("SELECT uc FROM UserCalendarEventEntity uc" +
            " JOIN FETCH uc.calendarEvent ce" +
            " JOIN FETCH ce.room" +
            " WHERE uc.user.userId = :userId")
    List<UserCalendarEventEntity> findListUserCalendar(@Param("userId") Long userId);
}
