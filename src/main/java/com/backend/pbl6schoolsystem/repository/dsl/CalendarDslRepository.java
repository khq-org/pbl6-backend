package com.backend.pbl6schoolsystem.repository.dsl;

import com.backend.pbl6schoolsystem.model.entity.CalendarEventEntity;
import com.backend.pbl6schoolsystem.model.entity.QCalendarEventEntity;
import com.backend.pbl6schoolsystem.request.calendar.ListCalendarRequest;
import com.backend.pbl6schoolsystem.security.UserPrincipal;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CalendarDslRepository {
    private final QCalendarEventEntity calendar = QCalendarEventEntity.calendarEventEntity;
    private final JPAQueryFactory queryFactory;

    public List<CalendarEventEntity> listCalendar(ListCalendarRequest request) {
        return null;
    }


}
