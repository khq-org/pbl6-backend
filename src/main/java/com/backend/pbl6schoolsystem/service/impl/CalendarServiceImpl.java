package com.backend.pbl6schoolsystem.service.impl;

import com.backend.pbl6schoolsystem.common.constant.ErrorCode;
import com.backend.pbl6schoolsystem.common.exception.NotFoundException;
import com.backend.pbl6schoolsystem.model.dto.calendar.CalendarEventDTO;
import com.backend.pbl6schoolsystem.model.entity.*;
import com.backend.pbl6schoolsystem.repository.dsl.CalendarDslRepository;
import com.backend.pbl6schoolsystem.repository.dsl.ClassCalendarDslRepository;
import com.backend.pbl6schoolsystem.repository.jpa.*;
import com.backend.pbl6schoolsystem.request.calendar.CreateUpdateCalendarRequest;
import com.backend.pbl6schoolsystem.request.calendar.ListCalendarRequest;
import com.backend.pbl6schoolsystem.response.ErrorResponse;
import com.backend.pbl6schoolsystem.response.NoContentResponse;
import com.backend.pbl6schoolsystem.response.OnlyIdResponse;
import com.backend.pbl6schoolsystem.response.calendar.ListCalendarResponse;
import com.backend.pbl6schoolsystem.security.UserPrincipal;
import com.backend.pbl6schoolsystem.service.CalendarService;
import com.backend.pbl6schoolsystem.util.RequestUtil;
import com.backend.pbl6schoolsystem.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {
    private final CalendarRepository calendarRepository;
    private final UserCalendarRepository userCalendarRepository;
    private final ClassCalendarRepository classCalendarRepository;
    private final ClassRepository classRepository;
    private final UserRepository userRepository;
    private final ClassCalendarDslRepository classCalendarDslRepository;
    private final CalendarDslRepository calendarDslRepository;

    @Override
    public ListCalendarResponse getListCalendar(ListCalendarRequest request) {
        request.setCalendarEvent(RequestUtil.blankIfNull(request.getCalendarEvent()));
        request.setUserId(RequestUtil.defaultIfNull(request.getUserId(), -1L));
        request.setClassId(RequestUtil.defaultIfNull(request.getClassId(), -1L));
        UserPrincipal principal = SecurityUtils.getPrincipal();
        List<CalendarEventEntity> calendarEvents = calendarDslRepository.listCalendar(request, principal.getUserId());
        return ListCalendarResponse.builder()
                .setSuccess(true)
                .setItems(calendarEvents.stream()
                        .map(c -> returnCalendarDTO(c))
                        .collect(Collectors.toList()))
                .build();
    }

    public CalendarEventDTO returnCalendarDTO(CalendarEventEntity entity) {
        CalendarEventDTO.CalendarEventDTOBuilder builder = CalendarEventDTO.builder();
        builder.setCalendarEventId(entity.getCalendarEventId())
                .setCalendarEvent(entity.getCalendarEvent())
                .setCalendarEventType(entity.getCalendarEventType())
                .setDayOfWeek(RequestUtil.blankIfNull(entity.getDayOfWeek()))
                .setRoomName(RequestUtil.blankIfNull(entity.getRoom().getRoom()))
                .setSubjectName(RequestUtil.blankIfNull(entity.getSubject().getSubject()))
                .setLessonStart(RequestUtil.defaultIfNull(entity.getLessonStart(), -1))
                .setLessonFinish(RequestUtil.defaultIfNull(entity.getLessonFinish(), -1));
        if (entity.getTimeStart() != null && entity.getTimeFinish() != null) {
            builder.setTimeStart(entity.getTimeStart());
            builder.setTimeFinish(entity.getTimeFinish());
        }
        return builder.build();
    }

    @Override
    public OnlyIdResponse createCalendar(CreateUpdateCalendarRequest request) {
        Map<String, String> errors = new HashMap<>();
        if (StringUtils.hasText(request.getCalendarEventName())) {
            errors.put("CalendarEventName", ErrorCode.MISSING_VALUE.name());
        }
        if (CollectionUtils.isEmpty(request.getClassIds()) && CollectionUtils.isEmpty(request.getUserIds())) {
            errors.put("Class or User", ErrorCode.MISSING_VALUE.name());
        }

        List<ClassCalendarEventEntity> classCalendarEvents = classCalendarDslRepository.findClassCalendar(request);

        if (!classCalendarEvents.isEmpty()) {
            for (ClassCalendarEventEntity classCalendarEvent : classCalendarEvents) {
                errors.put("Class " + classCalendarEvent.getClazz().getClazz(), ErrorCode.DUPLICATE_LESSON.name());
            }
        }

        if (!errors.isEmpty()) {
            return OnlyIdResponse.builder()
                    .setSuccess(false)
                    .setErrorResponse(ErrorResponse.builder()
                            .setErrors(errors)
                            .build())
                    .build();
        }

        UserPrincipal principal = SecurityUtils.getPrincipal();

        // calendar event
        CalendarEventEntity calendarEvent = new CalendarEventEntity();
        calendarEvent.setCalendarEvent(request.getCalendarEventName());
        calendarEvent.setCalendarEventType(request.getCalendarEventType());
        if (StringUtils.hasText(request.getDate())) {
            calendarEvent.setCalendarDate(LocalDate.parse(request.getDate()));
        }
        if (request.getLessonStart() != null && request.getLessonFinish() != null) {
            calendarEvent.setLessonStart(request.getLessonStart());
            calendarEvent.setLessonFinish(request.getLessonFinish());
        }
        if (StringUtils.hasText(request.getDayOfWeek())) {
            calendarEvent.setDayOfWeek(request.getDayOfWeek());
        }
        if (StringUtils.hasText(request.getTimeStart()) && StringUtils.hasText(request.getTimeFinish())) {
            calendarEvent.setTimeStart(LocalTime.parse(request.getTimeStart()));
            calendarEvent.setTimeFinish(LocalTime.parse(request.getTimeFinish()));
        }
        calendarEvent.setCreatedBy(userRepository.findById(principal.getUserId()).get());
        calendarEvent.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        calendarEvent = calendarRepository.save(calendarEvent);

        List<ClassCalendarEventEntity> classCalendarEventEntities = new ArrayList<>();
        List<UserCalendarEventEntity> userCalendarEventEntities = new ArrayList<>();

        if (!CollectionUtils.isEmpty(request.getClassIds())) {
            List<ClassEntity> classes = classRepository.findAllById(request.getClassIds());
            ClassCalendarEventEntity classCalendarEvent;
            for (ClassEntity clazz : classes) {
                classCalendarEvent = new ClassCalendarEventEntity();
                classCalendarEvent.setCalendarEvent(calendarEvent);
                classCalendarEvent.setClazz(clazz);
                classCalendarEventEntities.add(classCalendarEvent);
            }
        }

        if (!CollectionUtils.isEmpty(request.getUserIds())) {
            List<UserEntity> users = userRepository.findAllById(request.getUserIds());
            UserCalendarEventEntity userCalendarEvent;
            for (UserEntity user : users) {
                userCalendarEvent = new UserCalendarEventEntity();
                userCalendarEvent.setCalendarEvent(calendarEvent);
                userCalendarEvent.setUser(user);
                userCalendarEventEntities.add(userCalendarEvent);
            }
        }

        if (!classCalendarEventEntities.isEmpty()) {
            classCalendarRepository.saveAll(classCalendarEventEntities);
        }

        if (!userCalendarEventEntities.isEmpty()) {
            userCalendarRepository.saveAll(userCalendarEventEntities);
        }

        return OnlyIdResponse.builder()
                .setSuccess(true)
                .setId(calendarEvent.getCalendarEventId())
                .setName(calendarEvent.getCalendarEvent())
                .build();
    }

    @Override
    public OnlyIdResponse updateCalendar(Long calendarId, CreateUpdateCalendarRequest request) {
        return null;
    }

    @Override
    public NoContentResponse deleteCalendar(Long calendarId) {
        CalendarEventEntity calendarEvent = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new NotFoundException("Not found calendar event with id " + calendarId));
        calendarRepository.delete(calendarEvent);
        return NoContentResponse.builder()
                .setSuccess(true)
                .build();
    }
}
