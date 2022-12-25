package com.backend.pbl6schoolsystem.service.impl;

import com.backend.pbl6schoolsystem.common.constant.Constants;
import com.backend.pbl6schoolsystem.common.constant.ErrorCode;
import com.backend.pbl6schoolsystem.common.exception.NotFoundException;
import com.backend.pbl6schoolsystem.mapper.UserMapper;
import com.backend.pbl6schoolsystem.model.dto.calendar.CalendarEventDTO;
import com.backend.pbl6schoolsystem.model.dto.common.ClazzDTO;
import com.backend.pbl6schoolsystem.model.dto.common.GradeDTO;
import com.backend.pbl6schoolsystem.model.entity.*;
import com.backend.pbl6schoolsystem.repository.dsl.ClassCalendarDslRepository;
import com.backend.pbl6schoolsystem.repository.dsl.UserCalendarDslRepository;
import com.backend.pbl6schoolsystem.repository.jpa.*;
import com.backend.pbl6schoolsystem.request.calendar.ListCalendarRequest;
import com.backend.pbl6schoolsystem.request.user.ChangePasswordRequest;
import com.backend.pbl6schoolsystem.request.user.UpdateUserRequest;
import com.backend.pbl6schoolsystem.response.ErrorResponse;
import com.backend.pbl6schoolsystem.response.NoContentResponse;
import com.backend.pbl6schoolsystem.response.OnlyIdResponse;
import com.backend.pbl6schoolsystem.response.UserInfoResponse;
import com.backend.pbl6schoolsystem.response.calendar.ListCalendarResponse;
import com.backend.pbl6schoolsystem.response.clazz.ListClassResponse;
import com.backend.pbl6schoolsystem.security.CustomUser;
import com.backend.pbl6schoolsystem.security.UserPrincipal;
import com.backend.pbl6schoolsystem.service.UserService;
import com.backend.pbl6schoolsystem.util.RequestUtil;
import com.backend.pbl6schoolsystem.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StudentClazzRepository studentClazzRepository;
    private final TeacherClassRepository teacherClassRepository;
    private final ClassCalendarDslRepository classCalendarDslRepository;
    private final UserCalendarDslRepository userCalendarDslRepository;
    private final UserCalendarRepository userCalendarRepository;
    private final ClassCalendarRepository classCalendarRepository;
    private final String ADMIN = Constants.ADMIN_ROLE;
    private final String SCHOOL = Constants.SCHOOL_ROLE;
    private final String TEACHER = Constants.TEACHER_ROLE;
    private final String STUDENT = Constants.STUDENT_ROLE;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserInfoResponse getInfoAccount(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Not found user with id " + userId));
        List<String> authorities = new ArrayList<>();
        authorities.addAll(Arrays.asList(Constants.LOG_IN, Constants.LOG_OUT,
                Constants.UPDATE_PERSONAL_INFORMATION, Constants.CHANGE_PASSWORD));
        switch (user.getRole().getRole()) {
            case ADMIN:
                authorities.addAll(Arrays.asList(Constants.MANAGE_SCHOOL, Constants.MANAGE_SCHOOL_ADMIN));
                break;
            case SCHOOL:
                authorities.addAll(Arrays.asList(Constants.MANAGE_TEACHER, Constants.MANAGE_STUDENT, Constants.MANAGE_CLASS,
                        Constants.SETUP_CALENDAR, Constants.SETUP_INFORMATION_SCHOOL_YEAR));
                break;
            case TEACHER:
                authorities.addAll(Arrays.asList(Constants.MANAGE_STUDENT, Constants.SEE_CALENDAR, Constants.INPUT_SCORE,
                        Constants.MANAGE_CLASS));
                break;
            case STUDENT:
                authorities.addAll(Arrays.asList(Constants.SEE_SCORE, Constants.SEE_CALENDAR));
                break;
        }
        return UserInfoResponse.builder()
                .setSuccess(true)
                .setUser(UserMapper.entity2dto(user))
                .setAuthorities(authorities)
                .build();
    }

    @Override
    public OnlyIdResponse updateInfoAccount(Long userId, UpdateUserRequest request) {
        Map<String, String> errors = new HashMap<>();

        if (!StringUtils.hasText(request.getFirstName())) {
            errors.put("FirstName", ErrorCode.MISSING_VALUE.name());
        }
        if (!StringUtils.hasText(request.getLastName())) {
            errors.put("LastName", ErrorCode.MISSING_VALUE.name());
        }
        if (!StringUtils.hasText(request.getEmail())) {
            errors.put("Email", ErrorCode.MISSING_VALUE.name());
        }
        if (!StringUtils.hasText(request.getWorkingPosition())) {
            errors.put("WorkingPosition", ErrorCode.MISSING_VALUE.name());
        }

        if (!errors.isEmpty()) {
            return OnlyIdResponse.builder()
                    .setSuccess(false)
                    .setErrorResponse(ErrorResponse.builder()
                            .setErrors(errors)
                            .build())
                    .build();
        }

        UserEntity user = userRepository.findById(userId).get();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(RequestUtil.blankIfNull(request.getPhone()));
        user.setStreet(RequestUtil.blankIfNull(request.getStreet()));
        user.setDistrict(RequestUtil.blankIfNull(request.getDistrict()));
        user.setCity(RequestUtil.blankIfNull(request.getCity()));
        user.setPlaceOfBirth(RequestUtil.blankIfNull(request.getPlaceOfBirth()));
        user.setRole(roleRepository.findById(request.getRoleId()).get());
        user.setGender(Boolean.TRUE.equals(request.getGender()) ? Boolean.TRUE : Boolean.FALSE);
        user.setDateOfBirth(LocalDate.parse(request.getDateOfBirth() != null ? request.getDateOfBirth() : Constants.DEFAULT_DATE_OF_BIRTH));
        userRepository.save(user);

        return OnlyIdResponse.builder()
                .setSuccess(true)
                .setId(user.getUserId())
                .setName(user.getFirstName() + " " + user.getLastName())
                .build();
    }

    @Override
    public NoContentResponse changePassword(Long userId, ChangePasswordRequest request) {
        Map<String, String> errors = new HashMap<>();
        if (!StringUtils.hasText(request.getCurrentPassword())) {
            errors.put("CurrentPassword", ErrorCode.MISSING_VALUE.name());
        }
        if (!StringUtils.hasText(request.getNewPassword())) {
            errors.put("NewPassword", ErrorCode.MISSING_VALUE.name());
        }
        if (!StringUtils.hasText(request.getConfirmPassword())) {
            errors.put("ConfirmPassword", ErrorCode.MISSING_VALUE.name());
        }

        if (errors.isEmpty()) {
            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("Not found user with id " + userId));
            log.info(passwordEncoder.encode(request.getCurrentPassword()));
            if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
                errors.put("CurrentPassword", ErrorCode.INVALID_VALUE.name());
            }
            if (!request.getConfirmPassword().equals(request.getNewPassword())) {
                errors.put("ConfirmPassword", ErrorCode.INVALID_VALUE.name());
            }
            if (errors.isEmpty()) {
                user.setPassword(passwordEncoder.encode(request.getNewPassword()));
                userRepository.save(user);
                return NoContentResponse.builder()
                        .setSuccess(true)
                        .build();
            }
            return NoContentResponse.builder()
                    .setSuccess(false)
                    .setErrorResponse(ErrorResponse.builder()
                            .setErrors(errors)
                            .build())
                    .build();
        }

        return NoContentResponse.builder()
                .setSuccess(false)
                .setErrorResponse(ErrorResponse.builder()
                        .setErrors(errors)
                        .build())
                .build();
    }

    @Override
    public ListCalendarResponse getListCalendar(ListCalendarRequest request) {
        UserPrincipal principal = SecurityUtils.getPrincipal();
        List<ClassCalendarEventEntity> listClassCalendar;
        Map<String, String> errors = new HashMap<>();
        List<CalendarEventDTO> calendarEvents = new ArrayList<>();
        CalendarEventEntity calendarEvent;
        CalendarEventDTO calendarEventDTO;
        // Get list calendar STUDY for student
        if (principal.isStudent()) {
            if (request.getClassId() < 0) {
                errors.put("classId", ErrorCode.MISSING_VALUE.name());
            }
            if (!errors.isEmpty()) {
                return ListCalendarResponse.builder()
                        .setSuccess(false)
                        .setErrorResponse(ErrorResponse.builder()
                                .setErrors(errors)
                                .build())
                        .build();
            }
            StudentClazzEntity studentClazz = studentClazzRepository.findByStudentIdAndClazzId(principal.getUserId(), request.getClassId());
            request.setSchoolYearId(studentClazz.getSchoolYear().getSchoolYearId());
            listClassCalendar = classCalendarDslRepository.listClassCalendarEvent(request);
            List<UserEntity> teacherTeachClasses = userCalendarRepository.findByListCalendar(listClassCalendar.stream()
                            .map(cce -> cce.getCalendarEvent().getCalendarEventId()).collect(Collectors.toList()))
                    .stream().map(UserCalendarEventEntity::getUser).collect(Collectors.toList());
            UserEntity teacher;
            for (int i = 0; i < listClassCalendar.size(); i++) {
                calendarEvent = listClassCalendar.get(i).getCalendarEvent();
                teacher = teacherTeachClasses.get(i);
                calendarEventDTO = getCalendarEvent(calendarEvent);
                calendarEventDTO.setTeacher(CalendarEventDTO.Teacher.builder()
                        .setId(teacher.getUserId())
                        .setFirstName(teacher.getFirstName())
                        .setLastName(teacher.getLastName())
                        .build());
                calendarEvents.add(calendarEventDTO);
            }
        }

        // Get list teach of teacher
        if (principal.isTeacher()) {
            List<UserCalendarEventEntity> listTeacherCalendar = userCalendarDslRepository.findListUserCalendar(principal, request, true);
            List<ClassEntity> classes = classCalendarRepository.findByCalendarIds(listTeacherCalendar.stream()
                            .map(tt -> tt.getCalendarEvent().getCalendarEventId()).collect(Collectors.toList()))
                    .stream().map(ClassCalendarEventEntity::getClazz).collect(Collectors.toList());
            ClassEntity clazz;
            for (int i = 0; i < listTeacherCalendar.size(); i++) {
                calendarEvent = listTeacherCalendar.get(i).getCalendarEvent();
                clazz = classes.get(i);
                calendarEventDTO = getCalendarEvent(calendarEvent);
                calendarEventDTO.setCalendarEventType(Constants.TEACH);
                calendarEventDTO.setClazz(CalendarEventDTO.Clazz.builder()
                        .setId(clazz.getClassId())
                        .setName(clazz.getClazz())
                        .build());
                calendarEvents.add(calendarEventDTO);
            }
        }

        List<UserCalendarEventEntity> listUserCalendar = userCalendarDslRepository.findListUserCalendar(principal, request, false);
        listUserCalendar.removeIf(uc -> uc.getCalendarEvent().getCalendarEventType().equals(Constants.STUDY));
        calendarEvents.addAll(listUserCalendar.stream().map(uc -> getCalendarEvent(uc.getCalendarEvent())).collect(Collectors.toList()));
        return ListCalendarResponse.builder()
                .setSuccess(true)
                .setItems(calendarEvents.stream()
                        .filter(ce -> ce.getCalendarEventType().equalsIgnoreCase(request.getCalendarEventType()))
                        .collect(Collectors.toList()))
                .build();
    }


    @Override
    public ListClassResponse getListMyClass() {
        UserPrincipal principal = SecurityUtils.getPrincipal();
        List<ClassEntity> classes = new ArrayList<>();
        ListClassResponse.ListClassResponseBuilder builder = ListClassResponse.builder();
        builder.setSuccess(true);
        if (principal.isStudent()) {
            List<StudentClazzEntity> studentClasses = studentClazzRepository.findByStudentId(principal.getUserId());
            builder.setItems(studentClasses.stream()
                    .map(sc -> ClazzDTO.builder()
                            .setClassId(sc.getClazz().getClassId())
                            .setClazz(sc.getClazz().getClazz())
                            .setSpecializedClass(Boolean.TRUE.equals(sc.getClazz().getIsSpecializedClass()))
                            .setGrade(GradeDTO.builder()
                                    .setGradeId(sc.getClazz().getGrade().getGradeId())
                                    .setGrade(sc.getClazz().getGrade().getGrade())
                                    .build())
                            .setSubject(RequestUtil.blankIfNull(sc.getClazz().getSubject()))
                            .setSchoolYear(sc.getSchoolYear().getSchoolYear())
                            .build())
                    .collect(Collectors.toList()));
        } else if (principal.isTeacher()) {
            List<TeacherClassEntity> teacherClasses = teacherClassRepository.findByTeacher(principal.getUserId());
            builder.setItems(teacherClasses.stream()
                    .filter(tc -> Boolean.TRUE.equals(tc.getIsClassLeader()))
                    .map(tc -> ClazzDTO.builder()
                            .setClassId(tc.getClazz().getClassId())
                            .setClazz(tc.getClazz().getClazz())
                            .setSpecializedClass(Boolean.TRUE.equals(tc.getClazz().getIsSpecializedClass()))
                            .setGrade(GradeDTO.builder()
                                    .setGradeId(tc.getClazz().getGrade().getGradeId())
                                    .setGrade(tc.getClazz().getGrade().getGrade())
                                    .build())
                            .setSubject(RequestUtil.blankIfNull(tc.getClazz().getSubject()))
                            .setSchoolYear(tc.getSchoolYear().getSchoolYear())
                            .build())
                    .collect(Collectors.toList()));
        }
        return builder.build();
    }

    public CalendarEventDTO getCalendarEvent(CalendarEventEntity entity) {
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


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("Username invalid"));
        Collection<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        List.of(user.getRole()).forEach(role -> {
            simpleGrantedAuthorities.add(new SimpleGrantedAuthority(role.getRole()));
        });
        return new CustomUser(user.getUsername(), user.getPassword(), simpleGrantedAuthorities, user.getUserId(), user.getRole().getRole(),
                user.getFirstName(), user.getLastName(), (user.getSchool() != null) ? user.getSchool().getSchoolId() : -1L,
                RequestUtil.blankIfNull(user.getStreet()), RequestUtil.blankIfNull(user.getDistrict()), RequestUtil.blankIfNull(user.getCity()),
                user.getCreatedDate() != null ? user.getCreatedDate() : new Timestamp(System.currentTimeMillis()));
    }
}

