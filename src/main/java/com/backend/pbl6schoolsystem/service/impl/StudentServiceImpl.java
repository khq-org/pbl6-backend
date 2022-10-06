package com.backend.pbl6schoolsystem.service.impl;

import com.backend.pbl6schoolsystem.common.constant.ErrorCode;
import com.backend.pbl6schoolsystem.common.enums.UserRole;
import com.backend.pbl6schoolsystem.common.exception.NotFoundException;
import com.backend.pbl6schoolsystem.mapper.StudentMapper;
import com.backend.pbl6schoolsystem.model.entity.ParentStudentEntity;
import com.backend.pbl6schoolsystem.model.entity.SchoolEntity;
import com.backend.pbl6schoolsystem.model.entity.UserEntity;
import com.backend.pbl6schoolsystem.repository.dsl.SchoolDslRepository;
import com.backend.pbl6schoolsystem.repository.dsl.StudentDslRepository;
import com.backend.pbl6schoolsystem.repository.jpa.ParentStudentRepository;
import com.backend.pbl6schoolsystem.repository.jpa.RoleRepository;
import com.backend.pbl6schoolsystem.repository.jpa.SchoolRepository;
import com.backend.pbl6schoolsystem.repository.jpa.UserRepository;
import com.backend.pbl6schoolsystem.request.student.CreateStudentRequest;
import com.backend.pbl6schoolsystem.request.student.ListStudentRequest;
import com.backend.pbl6schoolsystem.response.ErrorResponse;
import com.backend.pbl6schoolsystem.response.OnlyIdResponse;
import com.backend.pbl6schoolsystem.response.PageResponse;
import com.backend.pbl6schoolsystem.response.student.GetStudentResponse;
import com.backend.pbl6schoolsystem.response.student.ListStudentResponse;
import com.backend.pbl6schoolsystem.service.StudentService;
import com.backend.pbl6schoolsystem.util.RequestUtil;
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
    private final SchoolRepository schoolRepository;
    private final RoleRepository roleRepository;
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
        if (request.getSchoolId() < 0) {
            errors.put("school", ErrorCode.MISSING_VALUE.name());
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
            SchoolEntity school = schoolRepository.findById(request.getSchoolId())
                    .orElseThrow(() -> new NotFoundException("Not found school with id " + request.getSchoolId()));
            String studentId = "std".concat(String.valueOf(schoolDslRepository.countStudentInSchool(school.getSchoolId()) + 1));
            String username = studentId.concat("@").concat(school.getSchool().replace(" ", "").toLowerCase());
            student.setStudentId(studentId);
            student.setRole(roleRepository.findById(UserRole.STUDENT_ROLE.getRoleId()).get());
            student.setUsername(username);
            student.setPassword(passwordEncoder.encode(username));
            student.setFirstName(request.getFirstName());
            student.setLastName(request.getLastName());
            student.setMiddleName(RequestUtil.blankIfNull(request.getMiddleName()));
            student.setPhone(RequestUtil.blankIfNull(request.getPhone()));
            student.setEmail(RequestUtil.blankIfNull(request.getEmail()));
            student.setStreet(RequestUtil.blankIfNull(request.getStreet()));
            student.setDistrict(RequestUtil.blankIfNull(request.getDistrict()));
            student.setCity(RequestUtil.blankIfNull(request.getCity()));
            student.setSchool(school);
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
                    parentEntity.setMiddleName(RequestUtil.blankIfNull(parent.getMiddleName()));
                    parentEntity.setPhone(RequestUtil.blankIfNull(parent.getPhone()));
                    parentEntity.setJob(RequestUtil.blankIfNull(parent.getJob()));
                    parentEntity.setStreet(RequestUtil.blankIfNull(parent.getStreet()));
                    parentEntity.setDistrict(RequestUtil.blankIfNull(parent.getDistrict()));
                    parentEntity.setCity(RequestUtil.blankIfNull(parent.getCity()));
                    listParent.add(parentEntity);
                }

                userRepository.save(student); // save student
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
    public ListStudentResponse getListStudent(ListStudentRequest request) {
        request.setSchoolId(RequestUtil.defaultIfNull(request.getSchoolId(), (long) -1));
        request.setGradeId(RequestUtil.defaultIfNull(request.getGradeId(), (long) -1));
        request.setClassId(RequestUtil.defaultIfNull(request.getClassId(), (long) -1));
        List<UserEntity> userEntities = studentDslRepository.getListStudent(request);
        return ListStudentResponse.builder()
                .setPageResponse(PageResponse.builder()
                        .setTotalItems((long) userEntities.size())
                        .setPage(request.getPage())
                        .setSize(request.getAll() ? Integer.MAX_VALUE : request.getSize())
                        .setTotalPages(request.getAll() ? 1 : RequestUtil.getTotalPages((long) userEntities.size(), request.getSize()))
                        .build())
                .setItems(userEntities.stream()
                        .map(ue -> StudentMapper.entity2dto(ue))
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public GetStudentResponse getStudent(Long studentId) {
        UserEntity student = userRepository.findOneById(studentId, UserRole.STUDENT_ROLE.getRoleId()).orElseThrow(() -> new NotFoundException("Not found student with id " + studentId));
        List<ParentStudentEntity> parentStudentEntities = parentStudentRepository.findParentStudentEntityByStudent(student.getUserId());
        List<UserEntity> parents = parentStudentEntities != null ? parentStudentEntities.stream()
                .map(ps -> userRepository.findById(ps.getParent().getUserId()).get())
                .collect(Collectors.toList()) : Collections.emptyList();

        return GetStudentResponse.builder()
                .setSuccess(true)
                .setStudentDetailDTO(StudentMapper.entity2DetailDto(student, parents))
                .build();
    }


}
