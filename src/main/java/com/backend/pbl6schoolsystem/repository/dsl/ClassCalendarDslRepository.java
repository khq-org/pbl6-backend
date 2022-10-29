package com.backend.pbl6schoolsystem.repository.dsl;

import com.backend.pbl6schoolsystem.model.entity.ClassCalendarEventEntity;
import com.backend.pbl6schoolsystem.model.entity.QClassCalendarEventEntity;
import com.backend.pbl6schoolsystem.request.calendar.CreateUpdateCalendarRequest;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ClassCalendarDslRepository {
    private final QClassCalendarEventEntity classCalendarEvent = QClassCalendarEventEntity.classCalendarEventEntity;
    private final JPAQueryFactory queryFactory;

    public List<ClassCalendarEventEntity> findClassCalendar(CreateUpdateCalendarRequest request) {
        JPAQuery<ClassCalendarEventEntity> query = queryFactory.select(classCalendarEvent)
                .from(classCalendarEvent)
                .where(classCalendarEvent.clazz.classId.in(request.getClassIds())
                        .and(classCalendarEvent.calendarEvent.dayOfWeek.eq(request.getDayOfWeek()))
                        .and(classCalendarEvent.calendarEvent.lessonStart.between(request.getLessonStart(), request.getLessonFinish())
                                .or(classCalendarEvent.calendarEvent.lessonFinish.between(request.getLessonStart(), request.getLessonFinish()))));
        query.leftJoin(classCalendarEvent.clazz).fetchJoin();
        return query.fetch();
    }

}
