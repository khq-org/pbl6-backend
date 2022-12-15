package com.backend.pbl6schoolsystem.service.impl;

import com.backend.pbl6schoolsystem.common.constant.Constants;
import com.backend.pbl6schoolsystem.common.constant.ErrorCode;
import com.backend.pbl6schoolsystem.common.enums.UserRole;
import com.backend.pbl6schoolsystem.common.exception.NotFoundException;
import com.backend.pbl6schoolsystem.mapper.UserMapper;
import com.backend.pbl6schoolsystem.model.dto.common.UserDTO;
import com.backend.pbl6schoolsystem.model.dto.student.LearningResultDTO;
import com.backend.pbl6schoolsystem.model.dto.student.ProfileStudentDTO;
import com.backend.pbl6schoolsystem.model.dto.student.StudentDTO;
import com.backend.pbl6schoolsystem.model.entity.*;
import com.backend.pbl6schoolsystem.repository.dsl.StudentDslRepository;
import com.backend.pbl6schoolsystem.repository.dsl.UserDslRepository;
import com.backend.pbl6schoolsystem.repository.jpa.*;
import com.backend.pbl6schoolsystem.request.leaningresult.CreateUpdateLearningResultRequest;
import com.backend.pbl6schoolsystem.request.student.CreateStudentRequest;
import com.backend.pbl6schoolsystem.request.student.GetProfileStudentRequest;
import com.backend.pbl6schoolsystem.request.student.ListStudentRequest;
import com.backend.pbl6schoolsystem.response.ErrorResponse;
import com.backend.pbl6schoolsystem.response.NoContentResponse;
import com.backend.pbl6schoolsystem.response.OnlyIdResponse;
import com.backend.pbl6schoolsystem.response.PageResponse;
import com.backend.pbl6schoolsystem.response.student.GetProfileStudentResponse;
import com.backend.pbl6schoolsystem.response.student.GetStudentResponse;
import com.backend.pbl6schoolsystem.response.user.ListUserResponse;
import com.backend.pbl6schoolsystem.security.UserPrincipal;
import com.backend.pbl6schoolsystem.service.StudentService;
import com.backend.pbl6schoolsystem.util.RequestUtil;
import com.backend.pbl6schoolsystem.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentDslRepository studentDslRepository;
    private final UserRepository userRepository;
    private final UserDslRepository userDslRepository;
    private final SchoolRepository schoolRepository;
    private final RoleRepository roleRepository;
    private final ClassRepository classRepository;
    private final StudentClazzRepository studentClazzRepository;
    private final UserCalendarRepository userCalendarRepository;
    private final ParentStudentRepository parentStudentRepository;
    private final LearningResultRepository learningResultRepository;
    private final ProfileStudentRepository profileStudentRepository;
    private final SchoolYearRepository schoolYearRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OnlyIdResponse createStudent(CreateStudentRequest request) {
        UserEntity student = new UserEntity();
        Map<String, String> errors = new HashMap<>();
        if (!StringUtils.hasText(request.getLastName())) {
            errors.put("lastName", ErrorCode.MISSING_VALUE.name());
        }
        if (!StringUtils.hasText(request.getFirstName())) {
            errors.put("firstName", ErrorCode.MISSING_VALUE.name());
        }
        if (request.getClassId() == null) {
            errors.put("classId", ErrorCode.MISSING_VALUE.name());
        } else {
            Optional<ClassEntity> clazz = classRepository.findById(request.getClassId());
            if (!clazz.isPresent()) {
                errors.put("classId", ErrorCode.NOT_FOUND.name());
            }
        }
        if (!errors.isEmpty()) {
            return OnlyIdResponse.builder()
                    .setSuccess(false)
                    .setErrorResponse(ErrorResponse.builder()
                            .setErrors(errors)
                            .build())
                    .build();
        } else {
            List<UserEntity> listParent = new ArrayList<>();
            UserPrincipal principal = SecurityUtils.getPrincipal();
            SchoolEntity school = schoolRepository.findById(principal.getSchoolId())
                    .orElseThrow(() -> new NotFoundException("Not found school with id " + request.getSchoolId()));
            UserEntity lastStudentInSchool = userDslRepository.getLastStudentTeacherInSchool(school.getSchoolId(), UserRole.STUDENT_ROLE.getRoleId());
            String studentId = Constants.USERNAME_STUDENT.concat(lastStudentInSchool != null ?
                    String.valueOf(Integer.valueOf(lastStudentInSchool.getStudentId().replace(Constants.USERNAME_STUDENT, "")) + 1) : "1");
            String username = studentId.concat("@").concat(school.getSchool().replace(" ", "").toLowerCase());
            student.setStudentId(studentId);
            student.setRole(roleRepository.findById(UserRole.STUDENT_ROLE.getRoleId()).get());
            student.setUsername(username);
            student.setPassword(passwordEncoder.encode(username));
            student.setFirstName(request.getFirstName());
            student.setLastName(request.getLastName());
            student.setPhone(RequestUtil.blankIfNull(request.getPhone()));
            student.setEmail(RequestUtil.blankIfNull(request.getEmail()));
            student.setStreet(RequestUtil.blankIfNull(request.getStreet()));
            student.setDistrict(RequestUtil.blankIfNull(request.getDistrict()));
            student.setCity(RequestUtil.blankIfNull(request.getCity()));
            student.setGender(Boolean.TRUE.equals(request.getGender()) ? Boolean.TRUE : Boolean.FALSE);
            student.setDateOfBirth(LocalDate.parse(request.getDateOfBirth() != null ? request.getDateOfBirth() : Constants.DEFAULT_DATE_OF_BIRTH));
            student.setPlaceOfBirth(RequestUtil.blankIfNull(request.getPlaceOfBirth()));
            student.setNationality(RequestUtil.blankIfNull(request.getNationality()));
            student.setSchool(school);
            student = userRepository.save(student); // save student
            UserEntity parentEntity;
            ParentStudentEntity parentStudentEntity;
            if (request.getParents() != null) {
                for (CreateStudentRequest.Parent parent : request.getParents()) {
                    if (!StringUtils.hasText(parent.getLastName())) {
                        errors.put("lastName", ErrorCode.MISSING_VALUE.name());
                    }
                    if (!StringUtils.hasText(parent.getFirstName())) {
                        errors.put("firstName", ErrorCode.MISSING_VALUE.name());
                    }
                    if (!errors.isEmpty()) {
                        return OnlyIdResponse.builder()
                                .setSuccess(false)
                                .setErrorResponse(ErrorResponse.builder()
                                        .setMessage("Parent missing value")
                                        .setErrors(errors)
                                        .build())
                                .build();
                    }
                    parentEntity = new UserEntity();
                    parentEntity.setFirstName(parent.getFirstName());
                    parentEntity.setLastName(parent.getLastName());
                    parentEntity.setPhone(RequestUtil.blankIfNull(parent.getPhone()));
                    parentEntity.setJob(RequestUtil.blankIfNull(parent.getJob()));
                    parentEntity.setStreet(RequestUtil.blankIfNull(parent.getStreet()));
                    parentEntity.setDistrict(RequestUtil.blankIfNull(parent.getDistrict()));
                    parentEntity.setCity(RequestUtil.blankIfNull(parent.getCity()));
                    parentEntity.setSchool(school);
                    listParent.add(parentEntity);
                }

                for (UserEntity parent : listParent) {
                    userRepository.save(parent); // save parent
                    parentStudentEntity = new ParentStudentEntity();
                    parentStudentEntity.setStudent(student);
                    parentStudentEntity.setParent(parent);
                    parentStudentRepository.save(parentStudentEntity);
                }
            }


            // save new profile student
            ProfileStudentEntity profileStudent = new ProfileStudentEntity();
            profileStudent.setStudent(student);
            profileStudent.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            profileStudent = profileStudentRepository.save(profileStudent);

            ClassEntity clazz = classRepository.findById(request.getClassId())
                    .orElseThrow(() -> new NotFoundException("Not found clazz with id " + request.getClassId()));

            // get current schoolYear;
            int year = LocalDate.now().getYear();
            String strSchoolYear = year + "-" + ++year;
            Optional<SchoolYearEntity> schoolYear = schoolYearRepository.findByName(strSchoolYear);
            SchoolYearEntity newSchoolYear = schoolYear.isPresent() ? schoolYear.get() : null;
            if (newSchoolYear == null) {
                newSchoolYear = new SchoolYearEntity();
                newSchoolYear.setSchoolYear(strSchoolYear);
                newSchoolYear = schoolYearRepository.save(newSchoolYear);
            }

            // save new learning result
            LearningResultEntity learningResult = new LearningResultEntity();
            learningResult.setProfileStudent(profileStudent);
            learningResult.setClazz(clazz);
            learningResult.setSchoolYear(newSchoolYear);
            learningResultRepository.save(learningResult);

            // save student class
            StudentClazzEntity studentClazz = new StudentClazzEntity();
            studentClazz.setStudent(student);
            studentClazz.setClazz(clazz);
            studentClazz.setSchoolYear(newSchoolYear);
            studentClazzRepository.save(studentClazz);

            return OnlyIdResponse.builder()
                    .setSuccess(true)
                    .setId(student.getUserId())
                    .setName(student.getFirstName() + " " + student.getLastName())
                    .build();
        }
    }

    @Override
    public OnlyIdResponse updateStudent(Long studentId, StudentDTO request) {
        UserEntity student = userRepository.findById(studentId).orElseThrow(() -> new NotFoundException("Student not found with id " + studentId));
        updateUser(student, request); // update student info
        userRepository.save(student);
        if (!request.getParents().isEmpty()) {
            UserEntity parent;
            for (UserDTO user : request.getParents()) {
                parent = userRepository.findById(user.getUserId()).get();
                parent.setFirstName(RequestUtil.blankIfNull(user.getFirstName()));
                parent.setLastName(RequestUtil.blankIfNull(user.getLastName()));
                parent.setPhone(RequestUtil.blankIfNull(user.getPhone()));
                parent.setStreet(RequestUtil.blankIfNull(user.getStreet()));
                parent.setDistrict(RequestUtil.blankIfNull(user.getDistrict()));
                parent.setCity(RequestUtil.blankIfNull(user.getCity()));
                parent.setJob(RequestUtil.blankIfNull(user.getJob()));
                userRepository.save(parent);
            }
        }

        return OnlyIdResponse.builder()
                .setSuccess(true)
                .setId(student.getUserId())
                .setName(student.getFirstName() + " " + student.getLastName())
                .build();
    }

    public void updateUser(UserEntity userEntity, StudentDTO request) {
        userEntity.setFirstName(request.getStudent().getFirstName());
        userEntity.setLastName(request.getStudent().getLastName());
        userEntity.setPhone(RequestUtil.blankIfNull(request.getStudent().getLastName()));
        userEntity.setEmail(RequestUtil.blankIfNull(request.getStudent().getEmail()));
        userEntity.setStreet(RequestUtil.blankIfNull(request.getStudent().getStreet()));
        userEntity.setDistrict(RequestUtil.blankIfNull(request.getStudent().getDistrict()));
        userEntity.setNationality(RequestUtil.blankIfNull(request.getStudent().getNationality()));
        userEntity.setGender(Boolean.TRUE.equals(request.getStudent().getGender()) ? Boolean.TRUE : Boolean.FALSE);
        userEntity.setCity(RequestUtil.blankIfNull(request.getStudent().getCity()));
        userEntity.setDateOfBirth(LocalDate.parse(request.getStudent().getDateOfBirth()));
        userEntity.setPlaceOfBirth(RequestUtil.blankIfNull(request.getStudent().getPlaceOfBirth()));
    }

    @Override
    public ListUserResponse getListStudent(ListStudentRequest request) {
        request.setGradeId(RequestUtil.defaultIfNull(request.getGradeId(), (long) -1));
        request.setClassId(RequestUtil.defaultIfNull(request.getClassId(), (long) -1));
        UserPrincipal principal = SecurityUtils.getPrincipal();
        List<UserEntity> userEntities = studentDslRepository.getListStudent(request, principal.getSchoolId());
        return ListUserResponse.builder()
                .setPageResponse(PageResponse.builder()
                        .setTotalItems((long) userEntities.size())
                        .setPage(request.getPage())
                        .setSize(request.getAll() ? Integer.MAX_VALUE : request.getSize())
                        .setTotalPages(request.getAll() ? 1 : RequestUtil.getTotalPages((long) userEntities.size(), request.getSize()))
                        .build())
                .setItems(userEntities.stream()
                        .map(UserMapper::entity2dto)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public GetStudentResponse getStudent(Long studentId) {
        UserPrincipal principal = SecurityUtils.getPrincipal();
        UserEntity student = userRepository.findOneById(studentId, UserRole.STUDENT_ROLE.getRoleId(), principal.getSchoolId())
                .orElseThrow(() -> new NotFoundException("Not found student with id " + studentId));
        List<UserEntity> parents = userRepository.findParentsByStudent(student.getUserId());
        List<ClassEntity> classes = classRepository.findClassesByStudent(student.getUserId());
        return GetStudentResponse.builder()
                .setSuccess(true)
                .setStudent(StudentDTO.builder()
                        .setStudent(UserMapper.entity2dto(student))
                        .setParents(parents != null ? parents.stream()
                                .map(UserMapper::entity2dto)
                                .collect(Collectors.toList()) : Collections.emptyList())
                        .setClasses(classes != null ? classes.stream()
                                .map(c -> StudentDTO.Clazz.builder()
                                        .setClazzId(c.getClassId())
                                        .setClazz(c.getClazz())
                                        .build())
                                .collect(Collectors.toList()) : Collections.emptyList())
                        .build())
                .build();
    }

    @Override
    public GetProfileStudentResponse getProfileStudent(GetProfileStudentRequest request) {
        UserPrincipal principal = SecurityUtils.getPrincipal();
        if (principal.isStudent()) {
            request.setStudentId(principal.getUserId());
        }

        if (request.getStudentId() == null) {
            return GetProfileStudentResponse.builder()
                    .setSuccess(false)
                    .setErrorResponse(ErrorResponse.builder()
                            .setErrors(new HashMap<>() {{
                                put("studentId", ErrorCode.MISSING_VALUE.name());
                            }})
                            .build())
                    .build();
        }

        Long studentId = request.getStudentId();
        ProfileStudentEntity profileStudent = profileStudentRepository.findByStudent(studentId)
                .orElseThrow(() -> new NotFoundException("Not found profile with studentId " + studentId));
        List<UserEntity> parents = userRepository.findParentsByStudent(studentId);
        List<LearningResultEntity> learningResults = learningResultRepository.findByProfileStudent(profileStudent.getProfileStudentId());
        return GetProfileStudentResponse.builder()
                .setSuccess(true)
                .setProfileStudentDTO(ProfileStudentDTO.builder()
                        .setProfileStudentId(profileStudent.getProfileStudentId())
                        .setStudent(ProfileStudentDTO.Student.builder()
                                .setStudentId(profileStudent.getStudent().getUserId())
                                .setFirstName(profileStudent.getStudent().getFirstName())
                                .setLastName(profileStudent.getStudent().getLastName())
                                .setDayOfBirth(profileStudent.getStudent().getDateOfBirth().toString())
                                .setPlaceOfBirth(profileStudent.getStudent().getPlaceOfBirth())
                                .setStreet(profileStudent.getStudent().getStreet())
                                .setDistrict(profileStudent.getStudent().getDistrict())
                                .setCity(profileStudent.getStudent().getCity())
                                .setParents(parents.stream()
                                        .map(p -> ProfileStudentDTO.Student.Parent.builder()
                                                .setParentId(p.getUserId())
                                                .setFirstName(p.getFirstName())
                                                .setLastName(p.getLastName())
                                                .setJob(RequestUtil.blankIfNull(p.getJob()))
                                                .setDayOfBirth(p.getDateOfBirth() != null ? p.getDateOfBirth().toString() : "")
                                                .build())
                                        .collect(Collectors.toList()))
                                .build())
                        .setLearningResults(!learningResults.isEmpty() ? learningResults.stream()
                                .map(lr -> LearningResultDTO.builder()
                                        .setLearningResultId(lr.getLearningResultId())
                                        .setSchoolYear(lr.getSchoolYear() != null ? lr.getSchoolYear().getSchoolYear() : null)
                                        .setClassName(lr.getClazz().getClazz())
                                        .setAverageScore(RequestUtil.defaultIfNull(lr.getAverageScore(), 0D))
                                        .setConduct(RequestUtil.blankIfNull(lr.getConduct()))
                                        .setLearningGrade(RequestUtil.blankIfNull(lr.getLearningGrading()))
                                        .setIsPassed(Boolean.TRUE.equals(lr.getIsPassed()) ? Boolean.TRUE : Boolean.FALSE)
                                        .build()).collect(Collectors.toList())
                                : Collections.emptyList())
                        .build())
                .build();
    }

    @Override
    public NoContentResponse deleteStudent(Long studentId) {
        UserEntity student = userRepository.findById(studentId).orElseThrow(() -> new NotFoundException("Not found student with id " + studentId));
        List<UserEntity> parents = userRepository.findParentsByStudent(studentId);
        List<StudentClazzEntity> listInStudentClass = studentClazzRepository.findByStudentId(studentId);
        List<UserCalendarEventEntity> listInUserCalendar = userCalendarRepository.findByStudentId(studentId);
        Optional<ProfileStudentEntity> profileStudent = profileStudentRepository.findByStudent(studentId);
        if (profileStudent.isPresent()) {
            List<LearningResultEntity> learningResults = learningResultRepository.findByProfileStudent(profileStudent.get().getProfileStudentId());
            if (!learningResults.isEmpty()) {
                learningResultRepository.deleteAll(learningResults);
            }
            profileStudentRepository.delete(profileStudent.get());
        }
        if (!listInStudentClass.isEmpty()) {
            studentClazzRepository.deleteAll(listInStudentClass);
        }
        if (!listInUserCalendar.isEmpty()) {
            userCalendarRepository.deleteAll(listInUserCalendar);
        }
        if (!parents.isEmpty()) {
            userRepository.deleteAll(parents);
        }
        userRepository.delete(student);
        return NoContentResponse.builder()
                .setSuccess(true)
                .build();
    }

//    @Override
//    public OnlyIdResponse addLearningResultForProfileStudent(Long profileStudentId, CreateUpdateLearningResultRequest request) {
//        ProfileStudentEntity profileStudent = profileStudentRepository.findById(profileStudentId)
//                .orElseThrow(() -> new NotFoundException("Not found profile student with id " + profileStudentId));
//
//        Map<String, String> errors = new HashMap<>();
//        if (request.getClassId() == null) {
//            errors.put("class", ErrorCode.MISSING_VALUE.name());
//        }
//
//        if (request.getSchoolYearId() == null) {
//            errors.put("schoolYear", ErrorCode.MISSING_VALUE.name());
//        }
//
//        Optional<LearningResultEntity> existLearningResult = learningResultRepository.findByProfileAndClazzAndSchoolYear(profileStudentId,
//                null, request.getClassId(), request.getSchoolYearId());
//        if (existLearningResult.isPresent()) {
//            errors.put("learningResult", ErrorCode.ALREADY_EXIST.name());
//        }
//
//        if (!errors.isEmpty()) {
//            return OnlyIdResponse.builder()
//                    .setSuccess(false)
//                    .setErrorResponse(ErrorResponse.builder()
//                            .setErrors(errors)
//                            .build())
//                    .build();
//        }
//
//        LearningResultEntity learningResult = new LearningResultEntity();
//        ClassEntity clazz = classRepository.findById(request.getClassId()).
//                orElseThrow(() -> new NotFoundException("Not found clazz with id " + request.getClassId()));
//        SchoolYearEntity schoolYear = schoolYearRepository.findById(request.getSchoolYearId())
//                .orElseThrow(() -> new NotFoundException("Not found school with id " + request.getSchoolYearId()));
//        learningResult.setProfileStudent(profileStudent);
//        learningResult.setClazz(clazz);
//        learningResult.setSchoolYear(schoolYear);
//        learningResult.setAverageScore(RequestUtil.defaultIfNull(request.getAverageScore(), 0D));
//        learningResult.setConduct(RequestUtil.defaultIfNull(request.getConduct(), ""));
//        learningResult = learningResultRepository.save(learningResult);
//
//        // save new student clazz
//        StudentClazzEntity studentClazz = new StudentClazzEntity();
//        studentClazz.setStudent(profileStudent.getStudent());
//        studentClazz.setClazz(clazz);
//        studentClazzRepository.save(studentClazz);
//
//        return OnlyIdResponse.builder()
//                .setSuccess(true)
//                .setId(learningResult.getLearningResultId())
//                .build();
//    }

    @Override
    public OnlyIdResponse updateLearningResultForProfileStudent(Long learningResultId, CreateUpdateLearningResultRequest request) {
        LearningResultEntity learningResult = learningResultRepository.findByLearningResult(learningResultId)
                .orElseThrow(() -> new NotFoundException("Not found learning result with id " + learningResultId));

        Map<String, String> errors = new HashMap<>();
        if (request.getClassId() == null) {
            errors.put("class", ErrorCode.MISSING_VALUE.name());
        }

        if (request.getSchoolYearId() == null) {
            errors.put("schoolYear", ErrorCode.MISSING_VALUE.name());
        }

        Optional<LearningResultEntity> existLearningResult = learningResultRepository.findByProfileAndClazzAndSchoolYear(learningResult.getProfileStudent().getProfileStudentId()
                , learningResultId, request.getClassId(), request.getSchoolYearId());
        if (existLearningResult.isPresent()) {
            errors.put("learningResult", ErrorCode.ALREADY_EXIST.name());
        }

        if (!errors.isEmpty()) {
            return OnlyIdResponse.builder()
                    .setSuccess(false)
                    .setErrorResponse(ErrorResponse.builder()
                            .setErrors(errors)
                            .build())
                    .build();
        }

        ClassEntity clazz = classRepository.findById(request.getClassId())
                .orElseThrow(() -> new NotFoundException("Not found clazz with id " + request.getClassId()));
        StudentClazzEntity studentClazz = studentClazzRepository.findByStudentIdAndClazzId(learningResult.getProfileStudent().getStudent().getUserId(),
                learningResult.getClazz().getClassId());

        learningResult.setClazz(clazz);
        learningResult.setSchoolYear(schoolYearRepository.findById(request.getSchoolYearId()).get());
        learningResult.setAverageScore(RequestUtil.defaultIfNull(request.getAverageScore(), 0D));
        learningResult.setConduct(RequestUtil.blankIfNull(request.getConduct()));
        studentClazz.setClazz(clazz);
        studentClazz.setSchoolYear(schoolYearRepository.findById(request.getSchoolYearId()).get());
        learningResultRepository.save(learningResult);
        studentClazzRepository.save(studentClazz);

        return OnlyIdResponse.builder()
                .setSuccess(true)
                .setId(learningResult.getLearningResultId())
                .build();
    }
}
