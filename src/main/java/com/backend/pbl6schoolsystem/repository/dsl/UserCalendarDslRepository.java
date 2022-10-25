package com.backend.pbl6schoolsystem.repository.dsl;

import com.backend.pbl6schoolsystem.model.entity.QUserCalendarEventEntity;
import com.backend.pbl6schoolsystem.model.entity.UserCalendarEventEntity;
import com.backend.pbl6schoolsystem.request.calendar.ListCalendarRequest;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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

}
