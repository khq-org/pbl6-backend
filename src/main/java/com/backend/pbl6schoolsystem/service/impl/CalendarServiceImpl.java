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
import com.backend.pbl6schoolsystem.repository.dsl.UserCalendarDslRepository;
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
    private final SubjectRepository subjectRepository;
    private final StudentClazzRepository studentClazzRepository;
    private final TeacherClassRepository teacherClassRepository;
    private final SchoolYearRepository schoolYearRepository;
    private final ClassCalendarDslRepository classCalendarDslRepository;
    private final UserCalendarDslRepository userCalendarDslRepository;
    private final CalendarDslRepository calendarDslRepository;

    @Override
    public ListCalendarResponse getListCalendar(ListCalendarRequest request) {
        request.setCalendarEvent(RequestUtil.blankIfNull(request.getCalendarEvent()));
        request.setUserId(RequestUtil.defaultIfNull(request.getUserId(), -1L));
        request.setClassId(RequestUtil.defaultIfNull(request.getClassId(), -1L));
        UserPrincipal principal = SecurityUtils.getPrincipal();
        List<CalendarEventEntity> calendarEvents = calendarDslRepository.listCalendar(request, principal);
        List<UserCalendarEventEntity> teacherCalendars;
        if (request.getClassId() > 0) {
            teacherCalendars = userCalendarRepository.findByListCalendar(calendarEvents.stream()
                    .map(ce -> ce.getCalendarEventId()).collect(Collectors.toList()));
            return ListCalendarResponse.builder()
                    .setSuccess(true)
                    .setItems(teacherCalendars.stream()
                            .map(tc -> returnClazzCalendar(tc))
                            .collect(Collectors.toList()))
                    .build();
        }
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
            Map<Long/*studentId*/, StudentClazzEntity> studentClazzMap = new HashMap<>();

            for (StudentClazzEntity sc : studentClasses) {
                if (!studentClazzMap.containsKey(sc.getStudent().getUserId())) {
                    studentClazzMap.put(sc.getStudent().getUserId(), sc);
                }
            }

            builder.setUsers(studentClazzMap.values().stream()
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

    public CalendarEventDTO returnClazzCalendar(UserCalendarEventEntity teacherClassCalendar) {
        CalendarEventDTO builder;
        builder = returnCalendarDTO(teacherClassCalendar.getCalendarEvent());
        builder.setTeacher(CalendarEventDTO.Teacher.builder()
                .setId(teacherClassCalendar.getUser().getUserId())
                .setFirstName(teacherClassCalendar.getUser().getFirstName())
                .setLastName(teacherClassCalendar.getUser().getLastName())
                .build());
        return builder;
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
            errors.put("calendarEventName", ErrorCode.MISSING_VALUE.name());
        }
        if (CollectionUtils.isEmpty(request.getClassIds()) && CollectionUtils.isEmpty(request.getUserIds())) {
            errors.put("classOrUser", ErrorCode.MISSING_VALUE.name());
        }
        if (request.getSchoolYearId() == null) {
            errors.put("schoolYearId", ErrorCode.MISSING_VALUE.name());
        }
        if (!StringUtils.hasText(request.getCalendarEventType())) {
            errors.put("calendarEventType", ErrorCode.MISSING_VALUE.name());
        } else if (List.of(Constants.STUDY, Constants.EXAMINATION).contains(request.getCalendarEventType())
                && request.getSubjectId() == null) {
            errors.put("subjectId", ErrorCode.MISSING_VALUE.name());
        }
        if (!CollectionUtils.isEmpty(request.getClassIds())) {
            if (request.getLessonFinish() == null) {
                request.setLessonFinish(request.getLessonStart());
            }
            List<ClassCalendarEventEntity> classCalendarEvents = classCalendarDslRepository.findClassCalendar(request);
            if (!classCalendarEvents.isEmpty()) {
                for (ClassCalendarEventEntity classCalendarEvent : classCalendarEvents) {
                    errors.put("Class " + classCalendarEvent.getClazz().getClazz(), ErrorCode.DUPLICATE_LESSON.name());
                }
            }
        }

        // FIX ME
        if (!Constants.STUDY.equalsIgnoreCase(request.getCalendarEventType()) && !CollectionUtils.isEmpty(request.getUserIds())) {
            List<UserCalendarEventEntity> userCalendarEvents = userCalendarDslRepository.findUserCalendar(request);
            if (!userCalendarEvents.isEmpty()) {
                for (UserCalendarEventEntity userCalendarEvent : userCalendarEvents) {
                    errors.put("User " + userCalendarEvent.getUser().getLastName() + userCalendarEvent.getUser().getFirstName(), ErrorCode.DUPLICATE_TIME.name());
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
        SchoolYearEntity schoolYear = schoolYearRepository.findById(request.getSchoolYearId()).get();

        // calendar event
        CalendarEventEntity calendarEvent = new CalendarEventEntity();
        calendarEvent.setCalendarEvent(request.getCalendarEventName());
        calendarEvent.setCalendarEventType(request.getCalendarEventType());
        if (StringUtils.hasText(request.getDate())) {
            calendarEvent.setCalendarDate(LocalDate.parse(request.getDate()));
        }
        if (request.getLessonStart() != null) {
            calendarEvent.setLessonStart(request.getLessonStart());
        }
        if (request.getLessonFinish() != null) {
            calendarEvent.setLessonFinish(request.getLessonFinish());
        }
        if (StringUtils.hasText(request.getDayOfWeek())) {
            calendarEvent.setDayOfWeek(request.getDayOfWeek());
        }
        if (StringUtils.hasText(request.getTimeStart()) && StringUtils.hasText(request.getTimeFinish())) {
            calendarEvent.setTimeStart(LocalTime.parse(request.getTimeStart()));
            calendarEvent.setTimeFinish(LocalTime.parse(request.getTimeFinish()));
        }
        if (List.of(Constants.STUDY, Constants.EXAMINATION).contains(request.getCalendarEventType()) && request.getSubjectId() != null
                && request.getSubjectId() > 0) {
            calendarEvent.setSubject(subjectRepository.findById(request.getSubjectId()).get());
        }
        calendarEvent.setCreatedBy(userRepository.findById(principal.getUserId()).get());
        calendarEvent.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        calendarEvent = calendarRepository.save(calendarEvent);

        List<ClassCalendarEventEntity> classCalendarEventEntities = new ArrayList<>();
        List<UserCalendarEventEntity> userCalendarEventEntities = new ArrayList<>();
        // for teacher class
        List<TeacherClassEntity> teacherClasses = new ArrayList<>();

        List<ClassEntity> classes = classRepository.findAllById(!CollectionUtils.isEmpty(request.getClassIds()) ?
                request.getClassIds() : Collections.emptyList());
        ClassCalendarEventEntity classCalendarEvent;
        for (ClassEntity clazz : classes) {
            classCalendarEvent = new ClassCalendarEventEntity();
            classCalendarEvent.setCalendarEvent(calendarEvent);
            classCalendarEvent.setClazz(clazz);
            classCalendarEventEntities.add(classCalendarEvent);
        }

        List<UserEntity> users = userRepository.findAllById(!CollectionUtils.isEmpty(request.getUserIds()) ?
                request.getUserIds() : Collections.emptyList());
        UserCalendarEventEntity userCalendarEvent;
        for (UserEntity user : users) {
            userCalendarEvent = new UserCalendarEventEntity();
            userCalendarEvent.setCalendarEvent(calendarEvent);
            userCalendarEvent.setUser(user);
            userCalendarEventEntities.add(userCalendarEvent);
        }

        if (!classes.isEmpty() && !users.isEmpty() && request.getCalendarEventType().equalsIgnoreCase(Constants.STUDY)
                && users.stream().allMatch(u -> u.getRole().getRoleId().equals(UserRole.TEACHER_ROLE.getRoleId()))) {
            TeacherClassEntity teacherClazz;
            for (int i = 0; i < classes.size(); i++) {
                teacherClazz = new TeacherClassEntity();
                teacherClazz.setTeacher(users.get(i));
                teacherClazz.setClazz(classes.get(i));
                teacherClazz.setIsClassLeader(false);
                teacherClazz.setSchoolYear(schoolYear);
                teacherClasses.add(teacherClazz);
            }
        }

        if (!teacherClasses.isEmpty()) {
            teacherClassRepository.saveAll(teacherClasses);
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
        SchoolYearEntity schoolYear = schoolYearRepository.findById(request.getSchoolYearId()).get();

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
        // for teacher class
        List<TeacherClassEntity> teacherClasses = new ArrayList<>();

        List<ClassCalendarEventEntity> listClazzForCalendarInDB = classCalendarRepository.findByCalendar(calendarEventId);
        List<ClassEntity> classesFromRequest = classRepository.findAllById(!CollectionUtils.isEmpty(request.getClassIds()) ?
                request.getClassIds() : Collections.emptyList());
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

        List<UserCalendarEventEntity> listUserForCalendarInDB = userCalendarRepository.findByCalendar(calendarEventId);
        List<UserEntity> usersFromRequest = userRepository.findAllById(!CollectionUtils.isEmpty(request.getUserIds()) ?
                request.getUserIds() : Collections.emptyList());
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

        if (!classesFromRequest.isEmpty() && !usersFromRequest.isEmpty() && request.getCalendarEventType().equalsIgnoreCase(Constants.STUDY)
                && usersFromRequest.stream().allMatch(u -> u.getRole().getRoleId().equals(UserRole.TEACHER_ROLE))) {
            TeacherClassEntity teacherClazz;
            for (int i = 0; i < usersFromRequest.size(); i++) {
                teacherClazz = new TeacherClassEntity();
                teacherClazz.setTeacher(usersFromRequest.get(i));
                teacherClazz.setClazz(classesFromRequest.get(i));
                teacherClazz.setIsClassLeader(false);
                teacherClazz.setSchoolYear(schoolYear);
                teacherClasses.add(teacherClazz);
            }
        }

        if (!listUserForCalendarInDB.isEmpty()) {
            userCalendarRepository.deleteAll(listUserForCalendarInDB);
        }

        if (!newListUserForCalendar.isEmpty()) {
            userCalendarRepository.saveAll(newListUserForCalendar);
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
