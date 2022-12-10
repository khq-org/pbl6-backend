package com.backend.pbl6schoolsystem.repository.dsl;

import com.backend.pbl6schoolsystem.common.constant.Constants;
import com.backend.pbl6schoolsystem.model.entity.ClassCalendarEventEntity;
import com.backend.pbl6schoolsystem.model.entity.QClassCalendarEventEntity;
import com.backend.pbl6schoolsystem.request.calendar.CreateUpdateCalendarRequest;
import com.backend.pbl6schoolsystem.request.calendar.ListCalendarRequest;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.aspectj.apache.bcel.classfile.Constant;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ClassCalendarDslRepository {
    private final QClassCalendarEventEntity classCalendarEvent = QClassCalendarEventEntity.classCalendarEventEntity;
    private final JPAQueryFactory queryFactory;

    public List<ClassCalendarEventEntity> findClassCalendar(CreateUpdateCalendarRequest request) {
        JPAQuery<ClassCalendarEventEntity> query = queryFactory.select(classCalendarEvent)
                .from(classCalendarEvent);
        BooleanExpression expressionCalendarType;
        BooleanExpression expressionDate;
        if (Constants.EXAMINATION.equalsIgnoreCase(request.getCalendarEventType())) {
            expressionCalendarType = classCalendarEvent.calendarEvent.timeStart.between(LocalTime.parse(request.getTimeStart()), LocalTime.parse(request.getTimeFinish()))
                    .or(classCalendarEvent.calendarEvent.timeFinish.between(LocalTime.parse(request.getTimeStart()), LocalTime.parse(request.getTimeFinish())));
            expressionDate = classCalendarEvent.calendarEvent.calendarDate.eq(LocalDate.parse(request.getDate()));
        } else {
            expressionCalendarType = classCalendarEvent.calendarEvent.lessonStart.between(request.getLessonStart(), request.getLessonFinish())
                    .or(classCalendarEvent.calendarEvent.lessonFinish.between(request.getLessonStart(), request.getLessonFinish()));
            expressionDate = classCalendarEvent.calendarEvent.dayOfWeek.eq(request.getDayOfWeek());
        }
        query.where(classCalendarEvent.clazz.classId.in(request.getClassIds())
                .and(expressionDate)
                .and(expressionCalendarType)
                .and(classCalendarEvent.semester.semesterId.eq(request.getSemesterId()))
                .and(classCalendarEvent.schoolYear.schoolYearId.eq(request.getSchoolYearId())));
        query.leftJoin(classCalendarEvent.clazz).fetchJoin();
        return query.fetch();
    }

    public List<ClassCalendarEventEntity> listClassCalendarEvent(ListCalendarRequest request) {
        JPAQuery<ClassCalendarEventEntity> query = queryFactory.select(classCalendarEvent)
                .from(classCalendarEvent)
                .where(classCalendarEvent.schoolYear.schoolYearId.eq(request.getSchoolYearId()))
                .where(classCalendarEvent.clazz.classId.eq(request.getClassId()));
        if (StringUtils.hasText(request.getCalendarEventType())) {
            query.where(classCalendarEvent.calendarEvent.calendarEventType.eq(request.getCalendarEventType()));
        }
        if (request.getSemesterId() != null && request.getSemesterId() > 0) {
            query.where(classCalendarEvent.semester.semesterId.eq(request.getSemesterId()));
        }
        query.leftJoin(classCalendarEvent.calendarEvent).fetch();
        return query.fetch();
    }
}
