package com.backend.pbl6schoolsystem.service.impl;

import com.backend.pbl6schoolsystem.common.constant.Constants;
import com.backend.pbl6schoolsystem.common.constant.ErrorCode;
import com.backend.pbl6schoolsystem.common.enums.UserRole;
import com.backend.pbl6schoolsystem.common.exception.NotFoundException;
import com.backend.pbl6schoolsystem.model.dto.calendar.CalendarEventDTO;
import com.backend.pbl6schoolsystem.model.dto.calendar.CalendarEventDetailDTO;
import com.backend.pbl6schoolsystem.model.entity.*;
import com.backend.pbl6schoolsystem.repository.dsl.CalendarDslRepository;
import com.backend.pbl6schoolsystem.repository.dsl.ClassCalendarDslRepository;
import com.backend.pbl6schoolsystem.repository.jpa.*;
import com.backend.pbl6schoolsystem.request.calendar.CreateUpdateCalendarRequest;
import com.backend.pbl6schoolsystem.request.calendar.ListCalendarRequest;
import com.backend.pbl6schoolsystem.response.ErrorResponse;
import com.backend.pbl6schoolsystem.response.NoContentResponse;
import com.backend.pbl6schoolsystem.response.OnlyIdResponse;
import com.backend.pbl6schoolsystem.response.calendar.GetCalendarEventResponse;
import com.backend.pbl6schoolsystem.response.calendar.ListCalendarResponse;
import com.backend.pbl6schoolsystem.security.UserPrincipal;
import com.backend.pbl6schoolsystem.service.CalendarService;
import com.backend.pbl6schoolsystem.util.RequestUtil;
import com.backend.pbl6schoolsystem.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.swing.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {
    private final CalendarRepository calendarRepository;
    private final UserCalendarRepository userCalendarRepository;
    private final ClassCalendarRepository classCalendarRepository;
    private final ClassRepository classRepository;
    private final UserRepository userRepository;
    private final StudentClazzRepository studentClazzRepository;
    private final ClassCalendarDslRepository classCalendarDslRepository;
    private final CalendarDslRepository calendarDslRepository;

    @Override
    public ListCalendarResponse getListCalendar(ListCalendarRequest request) {
        request.setCalendarEvent(RequestUtil.blankIfNull(request.getCalendarEvent()));
        request.setUserId(RequestUtil.defaultIfNull(request.getUserId(), -1L));
        request.setClassId(RequestUtil.defaultIfNull(request.getClassId(), -1L));
        UserPrincipal principal = SecurityUtils.getPrincipal();
        List<CalendarEventEntity> calendarEvents = calendarDslRepository.listCalendar(request, principal);
        return ListCalendarResponse.builder()
                .setSuccess(true)
                .setItems(calendarEvents.stream()
                        .map(c -> returnCalendarDTO(c))
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public GetCalendarEventResponse getCalendar(Long calendarEventId) {
        CalendarEventEntity calendarEvent = calendarRepository.findById(calendarEventId)
                .orElseThrow(() -> new NotFoundException("Not found calendar event with id " + calendarEventId));

        CalendarEventDetailDTO.CalendarEventDetailDTOBuilder builder = CalendarEventDetailDTO.builder();
        builder.setCalendarEvent(returnCalendarDTO(calendarEvent));

        List<ClassEntity> classes = classRepository.findClassesByCalendar(calendarEventId);
        List<Long> studentIds = userRepository.findUsersByCalendar(calendarEventId, UserRole.STUDENT_ROLE.getRoleId());
        List<Long> teacherIds = userRepository.findUsersByCalendar(calendarEventId, UserRole.TEACHER_ROLE.getRoleId());

        if (!classes.isEmpty()) {
            builder.setClasses(classes.stream()
                    .map(c -> CalendarEventDetailDTO.Clazz.builder()
                            .setClazzId(c.getClassId())
                            .setClazz(c.getClazz())
                            .setGrade(c.getGrade().getGrade())
                            .build())
                    .collect(Collectors.toList()));
        }

        if (!studentIds.isEmpty() && Constants.EXAMINATION.equalsIgnoreCase(calendarEvent.getCalendarEventType())) {
            List<StudentClazzEntity> studentClasses = studentClazzRepository.findByStudents(studentIds);
            builder.setUsers(studentClasses.stream()
                    .map(u -> CalendarEventDetailDTO.User.builder()
                            .setUserId(u.getStudent().getUserId())
                            .setFirstName(u.getStudent().getFirstName())
                            .setLastName(u.getStudent().getLastName())
                            .setDisplayName(u.getStudent().getLastName() + " " + u.getStudent().getFirstName())
                            .setClazzId(u.getClazz().getClassId())
                            .setClazz(u.getClazz().getClazz())
                            .build())
                    .collect(Collectors.toList()));
        }

        if (!teacherIds.isEmpty()) {
            List<UserEntity> teachers = userRepository.findAllById(teacherIds);
            builder.setUsers(teachers.stream()
                    .map(t -> CalendarEventDetailDTO.User.builder()
                            .setUserId(t.getUserId())
                            .setFirstName(t.getFirstName())
                            .setLastName(t.getLastName())
                            .setDisplayName(t.getLastName() + " " + t.getFirstName())
                            .setWorkingPosition(t.getWorkingPosition())
                            .build())
                    .collect(Collectors.toList()));
        }

        return GetCalendarEventResponse.builder()
                .setSuccess(true)
                .setCalendarEventDetail(builder.build())
                .build();
    }

    public CalendarEventDTO returnCalendarDTO(CalendarEventEntity entity) {
        CalendarEventDTO.CalendarEventDTOBuilder builder = CalendarEventDTO.builder();
        builder.setCalendarEventId(entity.getCalendarEventId())
                .setCalendarEvent(entity.getCalendarEvent())
                .setCalendarEventType(entity.getCalendarEventType())
                .setDayOfWeek(RequestUtil.blankIfNull(entity.getDayOfWeek()))
                .setLessonStart(RequestUtil.defaultIfNull(entity.getLessonStart(), -1))
                .setLessonFinish(RequestUtil.defaultIfNull(entity.getLessonFinish(), -1));
        if (entity.getTimeStart() != null && entity.getTimeFinish() != null) {
            builder.setTimeStart(entity.getTimeStart().toString());
            builder.setTimeFinish(entity.getTimeFinish().toString());
        }
        if (entity.getCalendarDate() != null) {
            builder.setCalendarDate(entity.getCalendarDate().toString());
        }
        if (entity.getRoom() != null) {
            builder.setRoomName(entity.getRoom().getRoom());
        }
        if (entity.getSubject() != null) {
            builder.setSubjectName(entity.getSubject().getSubject());
        }
        return builder.build();
    }

    public void checkValidRequest(CreateUpdateCalendarRequest request, Map<String, String> errors) {
        if (!StringUtils.hasText(request.getCalendarEventName())) {
            errors.put("CalendarEventName", ErrorCode.MISSING_VALUE.name());
        }
        if (CollectionUtils.isEmpty(request.getClassIds()) && CollectionUtils.isEmpty(request.getUserIds())) {
            errors.put("Class or User", ErrorCode.MISSING_VALUE.name());
        }

        if (!CollectionUtils.isEmpty(request.getClassIds())) {
            List<ClassCalendarEventEntity> classCalendarEvents = classCalendarDslRepository.findClassCalendar(request);
            if (!classCalendarEvents.isEmpty()) {
                for (ClassCalendarEventEntity classCalendarEvent : classCalendarEvents) {
                    errors.put("Class " + classCalendarEvent.getClazz().getClazz(), ErrorCode.DUPLICATE_LESSON.name());
                }
            }
        }
    }

    @Override
    public OnlyIdResponse createCalendar(CreateUpdateCalendarRequest request) {
        Map<String, String> errors = new HashMap<>();
        checkValidRequest(request, errors);
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
    public OnlyIdResponse updateCalendar(Long calendarEventId, CreateUpdateCalendarRequest request) {
        CalendarEventEntity calendarEvent = calendarRepository.findById(calendarEventId)
                .orElseThrow(() -> new NotFoundException("Not found calendar event with id " + calendarEventId));
        Map<String, String> errors = new HashMap<>();
        checkValidRequest(request, errors);
        if (!errors.isEmpty()) {
            return OnlyIdResponse.builder()
                    .setSuccess(false)
                    .setErrorResponse(ErrorResponse.builder()
                            .setErrors(errors)
                            .build())
                    .build();
        }

        UserPrincipal principal = SecurityUtils.getPrincipal();

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
        calendarEvent.setModifiedBy(userRepository.findById(principal.getUserId()).get());
        calendarEvent.setModifiedDate(new Timestamp(System.currentTimeMillis()));
        calendarRepository.save(calendarEvent);

        List<ClassCalendarEventEntity> newListClazzForCalendar = new ArrayList<>();
        List<UserCalendarEventEntity> newListUserForCalendar = new ArrayList<>();

        if (!CollectionUtils.isEmpty(request.getClassIds())) {
            List<ClassCalendarEventEntity> listClazzForCalendarInDB = classCalendarRepository.findByCalendar(calendarEventId);
            List<ClassEntity> classesFromRequest = classRepository.findAllById(request.getClassIds());
            for (ClassEntity clazzFromRequest : classesFromRequest) {
                boolean isExistClass = false;
                for (ClassCalendarEventEntity clazzCalendarEvent : listClazzForCalendarInDB) {
                    if (clazzCalendarEvent.getClazz().getClassId().equals(clazzFromRequest.getClassId())) {
                        isExistClass = true;
                        listClazzForCalendarInDB.remove(clazzCalendarEvent);
                        break;
                    }
                }
                if (!isExistClass) {
                    ClassCalendarEventEntity calendarEventEntity = new ClassCalendarEventEntity();
                    calendarEventEntity.setCalendarEvent(calendarEvent);
                    calendarEventEntity.setClazz(clazzFromRequest);
                    newListClazzForCalendar.add(calendarEventEntity);
                }
            }

            if (!listClazzForCalendarInDB.isEmpty()) {
                classCalendarRepository.deleteAll(listClazzForCalendarInDB);
            }

            if (!newListClazzForCalendar.isEmpty()) {
                classCalendarRepository.saveAll(newListClazzForCalendar);
            }
        }

        if (!CollectionUtils.isEmpty(request.getUserIds())) {
            List<UserCalendarEventEntity> listUserForCalendarInDB = userCalendarRepository.findByCalendar(calendarEventId);
            List<UserEntity> usersFromRequest = userRepository.findAllById(request.getUserIds());
            for (UserEntity userFromRequest : usersFromRequest) {
                boolean isExistUser = false;
                for (UserCalendarEventEntity userCalendarEvent : listUserForCalendarInDB) {
                    if (userCalendarEvent.getUser().getUserId().equals(userFromRequest.getUserId())) {
                        isExistUser = true;
                        listUserForCalendarInDB.remove(userCalendarEvent);
                        break;
                    }
                }
                if (!isExistUser) {
                    UserCalendarEventEntity userCalendarEventEntity = new UserCalendarEventEntity();
                    userCalendarEventEntity.setCalendarEvent(calendarEvent);
                    userCalendarEventEntity.setUser(userFromRequest);
                    newListUserForCalendar.add(userCalendarEventEntity);
                }
            }

            if (!listUserForCalendarInDB.isEmpty()) {
                userCalendarRepository.deleteAll(listUserForCalendarInDB);
            }

            if (!newListUserForCalendar.isEmpty()) {
                userCalendarRepository.saveAll(newListUserForCalendar);
            }
        }

        return OnlyIdResponse.builder()
                .setSuccess(true)
                .setId(calendarEvent.getCalendarEventId())
                .setName(calendarEvent.getCalendarEvent())
                .build();
    }

    @Override
    public NoContentResponse deleteCalendar(Long calendarEventId) {
        CalendarEventEntity calendarEvent = calendarRepository.findById(calendarEventId)
                .orElseThrow(() -> new NotFoundException("Not found calendar event with id " + calendarEventId));
        List<ClassCalendarEventEntity> classCalendarEvents = classCalendarRepository.findByCalendar(calendarEventId);
        List<UserCalendarEventEntity> userCalendarEvents = userCalendarRepository.findByCalendar(calendarEventId);
        if (!classCalendarEvents.isEmpty()) {
            classCalendarRepository.deleteAll(classCalendarEvents);
        }
        if (!userCalendarEvents.isEmpty()) {
            userCalendarRepository.deleteAll(userCalendarEvents);
        }
        calendarRepository.delete(calendarEvent);
        return NoContentResponse.builder()
                .setSuccess(true)
                .build();
    }
}
