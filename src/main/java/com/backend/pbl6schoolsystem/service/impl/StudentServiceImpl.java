package com.backend.pbl6schoolsystem.service.impl;

import com.backend.pbl6schoolsystem.common.constant.Constants;
import com.backend.pbl6schoolsystem.common.constant.ErrorCode;
import com.backend.pbl6schoolsystem.common.enums.UserRole;
import com.backend.pbl6schoolsystem.common.exception.NotFoundException;
import com.backend.pbl6schoolsystem.mapper.UserMapper;
import com.backend.pbl6schoolsystem.model.dto.common.UserDTO;
import com.backend.pbl6schoolsystem.model.dto.student.StudentDTO;
import com.backend.pbl6schoolsystem.model.entity.ClassEntity;
import com.backend.pbl6schoolsystem.model.entity.ParentStudentEntity;
import com.backend.pbl6schoolsystem.model.entity.SchoolEntity;
import com.backend.pbl6schoolsystem.model.entity.UserEntity;
import com.backend.pbl6schoolsystem.repository.dsl.SchoolDslRepository;
import com.backend.pbl6schoolsystem.repository.dsl.StudentDslRepository;
import com.backend.pbl6schoolsystem.repository.dsl.UserDslRepository;
import com.backend.pbl6schoolsystem.repository.jpa.*;
import com.backend.pbl6schoolsystem.request.student.CreateStudentRequest;
import com.backend.pbl6schoolsystem.request.student.ListStudentRequest;
import com.backend.pbl6schoolsystem.response.ErrorResponse;
import com.backend.pbl6schoolsystem.response.NoContentResponse;
import com.backend.pbl6schoolsystem.response.OnlyIdResponse;
import com.backend.pbl6schoolsystem.response.PageResponse;
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
    private final ParentStudentRepository parentStudentRepository;
    private final SchoolDslRepository schoolDslRepository;
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
        userEntity.setCity(RequestUtil.blankIfNull(request.getStudent().getCity()));
        userEntity.setDateOfBirth(request.getStudent().getDateOfBirth());
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
    public NoContentResponse deleteStudent(Long studentId) {
        UserEntity student = userRepository.findById(studentId).orElseThrow(() -> new NotFoundException("Not found student with id " + studentId));
        List<UserEntity> parents = userRepository.findParentsByStudent(studentId);
        if (!parents.isEmpty()) {
            userRepository.deleteAll(parents);
        }
        userRepository.delete(student);
        return NoContentResponse.builder()
                .setSuccess(true)
                .build();
    }
}
