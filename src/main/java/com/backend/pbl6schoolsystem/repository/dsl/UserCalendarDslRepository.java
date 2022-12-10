package com.backend.pbl6schoolsystem.repository.dsl;

import com.backend.pbl6schoolsystem.common.constant.Constants;
import com.backend.pbl6schoolsystem.common.enums.UserRole;
import com.backend.pbl6schoolsystem.model.entity.QUserCalendarEventEntity;
import com.backend.pbl6schoolsystem.model.entity.UserCalendarEventEntity;
import com.backend.pbl6schoolsystem.request.calendar.CreateUpdateCalendarRequest;
import com.backend.pbl6schoolsystem.request.calendar.ListCalendarRequest;
import com.backend.pbl6schoolsystem.security.UserPrincipal;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserCalendarDslRepository {
    private final QUserCalendarEventEntity userCalendarEvent = QUserCalendarEventEntity.userCalendarEventEntity;
    private final JPAQueryFactory queryFactory;

    public List<UserCalendarEventEntity> findUserCalendar(CreateUpdateCalendarRequest request) {
        JPAQuery<UserCalendarEventEntity> query = queryFactory.select(userCalendarEvent)
                .from(userCalendarEvent);
        BooleanExpression expressionCalendarType;
        BooleanExpression expressionDate;
        if (List.of(Constants.EXAMINATION, Constants.MEETING).contains(request.getCalendarEventType())) {
            expressionCalendarType = userCalendarEvent.calendarEvent.timeStart.between(LocalTime.parse(request.getTimeStart()), LocalTime.parse(request.getTimeFinish()))
                    .or(userCalendarEvent.calendarEvent.timeFinish.between(LocalTime.parse(request.getTimeStart()), LocalTime.parse(request.getTimeFinish())));
            expressionDate = userCalendarEvent.calendarEvent.calendarDate.eq(LocalDate.parse(request.getDate()));
        } else {
            expressionCalendarType = userCalendarEvent.calendarEvent.lessonStart.between(request.getLessonStart(), request.getLessonFinish())
                    .or(userCalendarEvent.calendarEvent.lessonFinish.between(request.getLessonStart(), request.getLessonFinish()));
            expressionDate = userCalendarEvent.calendarEvent.dayOfWeek.eq(request.getDayOfWeek());
        }
        query.where(userCalendarEvent.user.userId.in(request.getUserIds())
                .and(expressionDate)
                .and(expressionCalendarType)
                .and(userCalendarEvent.semester.semesterId.eq(request.getSemesterId()))
                .and(userCalendarEvent.schoolYear.schoolYearId.eq(request.getSchoolYearId())));
        query.leftJoin(userCalendarEvent.user).fetchJoin();
        return query.fetch();
    }

    public List<UserCalendarEventEntity> findListUserCalendar(UserPrincipal principal, ListCalendarRequest request, Boolean isTeach) {
        JPAQuery<UserCalendarEventEntity> query = queryFactory.select(userCalendarEvent)
                .from(userCalendarEvent)
                .where(userCalendarEvent.user.userId.eq(principal.getUserId()));
        if (isTeach) {
            query.where(userCalendarEvent.calendarEvent.calendarEventType.eq(Constants.STUDY));
        } else {
            if (StringUtils.hasText(request.getCalendarEventType()) && List.of(Constants.STUDY, Constants.EXAMINATION, Constants.MEETING, Constants.TEACH).contains(request.getCalendarEventType())) {
                query.where(userCalendarEvent.calendarEvent.calendarEventType.eq(request.getCalendarEventType()));
            }
        }
        if (request.getSchoolYearId() != null && request.getSchoolYearId() > 0) {
            query.where(userCalendarEvent.schoolYear.schoolYearId.eq(request.getSchoolYearId()));
        }
        if (request.getSemesterId() != null && request.getSemesterId() > 0) {
            query.where(userCalendarEvent.semester.semesterId.eq(request.getSemesterId()));
        }
        if (StringUtils.hasText(request.getCalendarEvent())) {
            query.where(userCalendarEvent.calendarEvent.calendarEvent.containsIgnoreCase(request.getCalendarEvent()));
        }
        query.leftJoin(userCalendarEvent.calendarEvent).fetch();
        return query.fetch();
    }
}
