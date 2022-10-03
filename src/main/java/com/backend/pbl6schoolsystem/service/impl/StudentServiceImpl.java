package com.backend.pbl6schoolsystem.service.impl;

import com.backend.pbl6schoolsystem.common.constant.ErrorCode;
import com.backend.pbl6schoolsystem.common.exception.NotFoundException;
import com.backend.pbl6schoolsystem.mapper.SchoolMapper;
import com.backend.pbl6schoolsystem.mapper.StudentMapper;
import com.backend.pbl6schoolsystem.model.dto.student.StudentDTO;
import com.backend.pbl6schoolsystem.model.entity.SchoolEntity;
import com.backend.pbl6schoolsystem.model.entity.UserEntity;
import com.backend.pbl6schoolsystem.repository.dsl.StudentDslRepository;
import com.backend.pbl6schoolsystem.repository.jpa.SchoolRepository;
import com.backend.pbl6schoolsystem.request.student.CreateStudentRequest;
import com.backend.pbl6schoolsystem.request.student.ListStudentRequest;
import com.backend.pbl6schoolsystem.response.ErrorResponse;
import com.backend.pbl6schoolsystem.response.OnlyIdResponse;
import com.backend.pbl6schoolsystem.response.PageResponse;
import com.backend.pbl6schoolsystem.response.school.ListSchoolResponse;
import com.backend.pbl6schoolsystem.response.student.ListStudentResponse;
import com.backend.pbl6schoolsystem.service.StudentService;
import com.backend.pbl6schoolsystem.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentDslRepository studentDslRepository;
    private final SchoolRepository schoolRepository;

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
            SchoolEntity school = schoolRepository.findById(request.getSchoolId())
                    .orElseThrow(() -> new NotFoundException("Not found school with id " + request.getSchoolId()));
            student.setUsername(generateUsername(request, school));
            return OnlyIdResponse.builder()
                    .setSuccess(false)
                    .setErrorResponse(ErrorResponse.builder()
                            .setErrors(errors)
                            .build())
                    .build();
        }
    }

    public String generateUsername(CreateStudentRequest request, SchoolEntity school) {
        String username = new String(request.getFirstName().toLowerCase()
                .concat(".")
                .concat(request.getLastName()).toLowerCase().concat("@"));
        return username;
    }


    @Override
    public ListStudentResponse getListStudent(ListStudentRequest request) {
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
}
