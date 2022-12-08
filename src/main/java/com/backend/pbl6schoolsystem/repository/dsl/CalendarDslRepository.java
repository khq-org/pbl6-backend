package com.backend.pbl6schoolsystem.repository.dsl;

import com.backend.pbl6schoolsystem.common.constant.Constants;
import com.backend.pbl6schoolsystem.model.entity.CalendarEventEntity;
import com.backend.pbl6schoolsystem.model.entity.QCalendarEventEntity;
import com.backend.pbl6schoolsystem.model.entity.QClassCalendarEventEntity;
import com.backend.pbl6schoolsystem.model.entity.QUserCalendarEventEntity;
import com.backend.pbl6schoolsystem.request.calendar.ListCalendarRequest;
import com.backend.pbl6schoolsystem.security.UserPrincipal;
import com.backend.pbl6schoolsystem.util.RequestUtil;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CalendarDslRepository {
    private final QCalendarEventEntity calendar = QCalendarEventEntity.calendarEventEntity;
    private final QUserCalendarEventEntity userCalendar = QUserCalendarEventEntity.userCalendarEventEntity;
    private final QClassCalendarEventEntity classCalendar = QClassCalendarEventEntity.classCalendarEventEntity;
    private final JPAQueryFactory queryFactory;

    public List<CalendarEventEntity> listCalendar(ListCalendarRequest request, UserPrincipal principal) {
        JPAQuery<CalendarEventEntity> query = queryFactory.select(calendar)
                .from(calendar);
        if (request.getClassId() > 0) {
            query.innerJoin(classCalendar).on(classCalendar.calendarEvent.calendarEventId.eq(calendar.calendarEventId))
                    .where(classCalendar.clazz.classId.eq(request.getClassId()));
            if (request.getSchoolYearId() != null && request.getSchoolYearId() > 0) {
                query.where(classCalendar.schoolYear.schoolYearId.eq(request.getSchoolYearId()));
            }
            if (request.getSemesterId() != null && request.getSemesterId() > 0) {
                query.where(classCalendar.semester.semesterId.eq(request.getSemesterId()));
            }
        }
        if (request.getUserId() > 0) {
            query.innerJoin(userCalendar).on(userCalendar.calendarEvent.calendarEventId.eq(calendar.calendarEventId))
                    .where(userCalendar.user.userId.eq(request.getUserId()));
            if (request.getSchoolYearId() != null && request.getSchoolYearId() > 0) {
                query.where(userCalendar.schoolYear.schoolYearId.eq(request.getSchoolYearId()));
            }
            if (request.getSemesterId() != null && request.getSemesterId() > 0) {
                query.where(userCalendar.semester.semesterId.eq(request.getSemesterId()));
            }
        }
        if (StringUtils.hasText(request.getCalendarEvent())) {
            query.where(calendar.calendarEvent.containsIgnoreCase(request.getCalendarEvent()));
        }
        if (request.getCalendarType().equalsIgnoreCase(Constants.STUDY)
                || request.getCalendarType().equalsIgnoreCase(Constants.MEETING)
                || request.getCalendarType().equalsIgnoreCase(Constants.EXAMINATION)) {
            query.where(calendar.calendarEventType.equalsIgnoreCase(request.getCalendarType()));
        }

        query.where(calendar.createdBy.school.schoolId.eq(principal.getSchoolId()));
        query.leftJoin(calendar.room).fetchJoin();
        query.leftJoin(calendar.subject).fetchJoin();

        int page = RequestUtil.getPage(request.getPage());
        int size = RequestUtil.getSize(request.getSize());
        int offset = page * size;
        String sort = request.getSort();
        Order order = Order.DESC.name().equalsIgnoreCase(request.getDirection()) ? Order.DESC : Order.ASC;
        if ("name".equalsIgnoreCase(sort)) {
            query.orderBy(new OrderSpecifier<>(order, calendar.calendarEvent));
        }
        if (!request.getAll()) {
            query.limit(size);
        }
        query.offset(offset);

        return query.fetch();
    }

}
