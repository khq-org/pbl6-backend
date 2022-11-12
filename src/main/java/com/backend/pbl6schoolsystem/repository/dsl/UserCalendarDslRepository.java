package com.backend.pbl6schoolsystem.repository.dsl;

import com.backend.pbl6schoolsystem.model.entity.QUserCalendarEventEntity;
import com.backend.pbl6schoolsystem.model.entity.UserCalendarEventEntity;
import com.backend.pbl6schoolsystem.request.calendar.CreateUpdateCalendarRequest;
import com.backend.pbl6schoolsystem.request.calendar.ListCalendarRequest;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserCalendarDslRepository {
    private final QUserCalendarEventEntity userCalendarEvent = QUserCalendarEventEntity.userCalendarEventEntity;
    private final JPAQueryFactory queryFactory;

    public List<UserCalendarEventEntity> listUserCalendarEvent(ListCalendarRequest request) {
        JPAQuery<UserCalendarEventEntity> query = queryFactory.select(userCalendarEvent)
                .from(userCalendarEvent);
        return null;
    }

    public List<UserCalendarEventEntity> findUserCalendar(CreateUpdateCalendarRequest request) {
        JPAQuery<UserCalendarEventEntity> query = queryFactory.select(userCalendarEvent)
                .from(userCalendarEvent)
                .where(userCalendarEvent.user.userId.in(request.getUserIds())
                        .and(userCalendarEvent.calendarEvent.calendarDate.eq(LocalDate.parse(request.getDate())))
                        .and(userCalendarEvent.calendarEvent.timeStart.between(LocalTime.parse(request.getTimeStart()), LocalTime.parse(request.getTimeFinish()))
                                .or(userCalendarEvent.calendarEvent.timeFinish.between(LocalTime.parse(request.getTimeStart()), LocalTime.parse(request.getTimeFinish())))));
        query.leftJoin(userCalendarEvent.user).fetchJoin();
        return query.fetch();
    }
}
