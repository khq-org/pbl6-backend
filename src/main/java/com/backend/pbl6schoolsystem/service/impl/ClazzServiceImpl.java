package com.backend.pbl6schoolsystem.service.impl;

import com.backend.pbl6schoolsystem.common.constant.ErrorCode;
import com.backend.pbl6schoolsystem.common.exception.NotFoundException;
import com.backend.pbl6schoolsystem.model.dto.common.ClazzDTO;
import com.backend.pbl6schoolsystem.model.dto.common.GradeDTO;
import com.backend.pbl6schoolsystem.model.entity.ClassEntity;
import com.backend.pbl6schoolsystem.model.entity.TeacherClassEntity;
import com.backend.pbl6schoolsystem.repository.dsl.ClassDslRepository;
import com.backend.pbl6schoolsystem.repository.dsl.TeacherClassDslRepository;
import com.backend.pbl6schoolsystem.repository.jpa.ClassRepository;
import com.backend.pbl6schoolsystem.repository.jpa.GradeRepository;
import com.backend.pbl6schoolsystem.repository.jpa.SchoolRepository;
import com.backend.pbl6schoolsystem.request.clazz.CreateUpdateClassRequest;
import com.backend.pbl6schoolsystem.request.clazz.ListClassRequest;
import com.backend.pbl6schoolsystem.response.ErrorResponse;
import com.backend.pbl6schoolsystem.response.NoContentResponse;
import com.backend.pbl6schoolsystem.response.OnlyIdResponse;
import com.backend.pbl6schoolsystem.response.clazz.GetClassResponse;
import com.backend.pbl6schoolsystem.response.clazz.ListClassResponse;
import com.backend.pbl6schoolsystem.security.UserPrincipal;
import com.backend.pbl6schoolsystem.service.ClazzService;
import com.backend.pbl6schoolsystem.util.RequestUtil;
import com.backend.pbl6schoolsystem.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClazzServiceImpl implements ClazzService {
    private final ClassDslRepository classDslRepository;
    private final TeacherClassDslRepository teacherClassDslRepository;
    private final ClassRepository classRepository;

    private final SchoolRepository schoolRepository;

    private final GradeRepository gradeRepository;

    @Override
    public ListClassResponse getListClass(ListClassRequest request) {
        UserPrincipal principal = SecurityUtils.getPrincipal();
        request.setSchoolId(principal.getSchoolId());
        request.setClazzName(RequestUtil.blankIfNull(request.getClazzName()));
        request.setGradeId(RequestUtil.defaultIfNull(request.getGradeId(), -1L));
        if (request.getSchoolYearId() == null) {
            List<ClassEntity> listClazz = classDslRepository.getListClass(request);
            return ListClassResponse.builder()
                    .setSuccess(true)
                    .setItems(listClazz.stream()
                            .map(c -> toBuilder(c))
                            .collect(Collectors.toList()))
                    .build();
        }
        List<TeacherClassEntity> teacherClasses = teacherClassDslRepository.listTeacherClass(request);
        return ListClassResponse.builder()
                .setSuccess(true)
                .setItems(teacherClasses.stream()
                        .map(lc -> ClazzDTO.builder()
                                .setClassId(lc.getClazz().getClassId())
                                .setClazz(lc.getClazz().getClazz())
                                .setGrade(GradeDTO.builder()
                                        .setGradeId(lc.getClazz().getGrade().getGradeId())
                                        .setGrade(lc.getClazz().getGrade().getGrade())
                                        .build())
                                .setTeacher(ClazzDTO.Teacher.builder()
                                        .setTeacherId(lc.getTeacher().getUserId())
                                        .setTeacher(lc.getTeacher().getFirstName() + " " + lc.getTeacher().getLastName())
                                        .build())
                                .setSpecializedClass(lc.getClazz().getIsSpecializedClass() == null ? false
                                        : lc.getClazz().getIsSpecializedClass())
                                .setSubject(RequestUtil.blankIfNull(lc.getClazz().getSubject()))
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public GetClassResponse getClass(Long clazzId) {
        ClassEntity clazz = classRepository.findById(clazzId)
                .orElseThrow(() -> new NotFoundException("Not found class with id " + clazzId));
        return GetClassResponse.builder()
                .setSuccess(true)
                .setClazzDTO(toBuilder(clazz))
                .build();
    }

    public ClazzDTO toBuilder(ClassEntity clazz) {
        ClazzDTO.ClazzDTOBuilder builder = ClazzDTO.builder();
        builder.setClassId(clazz.getClassId())
                .setClazz(clazz.getClazz())
                .setGrade(GradeDTO.builder()
                        .setGradeId(clazz.getGrade().getGradeId())
                        .setGrade(clazz.getGrade().getGrade())
                        .build())
                .setSpecializedClass(clazz.getIsSpecializedClass() == null ? false : clazz.getIsSpecializedClass())
                .setSubject(RequestUtil.blankIfNull(clazz.getSubject()));
        return builder.build();
    }

    @Override
    public OnlyIdResponse createClass(CreateUpdateClassRequest request) {
        UserPrincipal principal = SecurityUtils.getPrincipal();
        Map<String, String> errors = new HashMap<>();
        checkValidInput(errors, request, principal, false, null);
        if (!errors.isEmpty()) {
            return OnlyIdResponse.builder()
                    .setSuccess(false)
                    .setErrorResponse(ErrorResponse.builder()
                            .setErrors(errors)
                            .build())
                    .build();
        }

        ClassEntity clazz = new ClassEntity();
        clazz.setClazz(request.getClassName());
        clazz.setGrade(gradeRepository.findById(request.getGradeId()).get());
        clazz.setSchool(schoolRepository.findById(principal.getSchoolId()).get());
        clazz.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        clazz.setCreatedBy(principal.getUserId());
        if (request.getIsSpecializedClass()) {
            clazz.setIsSpecializedClass(true);
            clazz.setSubject(request.getSubject());
        }
        classRepository.save(clazz);
        return OnlyIdResponse.builder()
                .setSuccess(true)
                .setId(clazz.getClassId())
                .setName(clazz.getClazz())
                .build();
    }

    @Override
    public OnlyIdResponse updateClass(Long clazzId, CreateUpdateClassRequest request) {
        ClassEntity clazz = classRepository.findById(clazzId).orElseThrow(() -> new NotFoundException("Not found class with id " + clazzId));
        UserPrincipal principal = SecurityUtils.getPrincipal();
        Map<String, String> errors = new HashMap<>();
        checkValidInput(errors, request, principal, true, clazz);
        if (!errors.isEmpty()) {
            return OnlyIdResponse.builder()
                    .setSuccess(false)
                    .setErrorResponse(ErrorResponse.builder()
                            .setErrors(errors)
                            .build())
                    .build();
        }

        clazz.setClazz(request.getClassName());
        clazz.setGrade(gradeRepository.findById(request.getGradeId()).get());
        clazz.setModifiedDate(new Timestamp(System.currentTimeMillis()));
        clazz.setModifiedBy(principal.getUserId());
        clazz.setIsSpecializedClass(request.getIsSpecializedClass());
        clazz.setSubject(request.getIsSpecializedClass() ? request.getSubject() : null);
        classRepository.save(clazz);

        return OnlyIdResponse.builder()
                .setSuccess(true)
                .setId(clazz.getClassId())
                .setName(clazz.getClazz())
                .build();
    }

    @Override
    public NoContentResponse deleteClass(Long clazzId) {
        ClassEntity clazz = classRepository.findById(clazzId).orElseThrow(() -> new NotFoundException("Not found class with id " + clazzId));
        classRepository.delete(clazz);
        return NoContentResponse.builder()
                .setSuccess(true)
                .build();
    }

    public void checkValidInput(Map<String, String> errors, CreateUpdateClassRequest request, UserPrincipal principal,
                                Boolean isUpdateClazz, ClassEntity currentClazz) {
        if (!StringUtils.hasText(request.getClassName())) {
            errors.put("ClassName", request.getClassName());
        }
        if (request.getGradeId() < 0) {
            errors.put("Grade", ErrorCode.INVALID_VALUE.name());
        }
        if (StringUtils.hasText(request.getClassName())) {
            ClassEntity clazz = classRepository.findClassByName(request.getClassName(), principal.getSchoolId()).orElse(null);
            if (isUpdateClazz) {
                if (clazz != null && !clazz.getClazz().equals(currentClazz.getClazz())) {
                    errors.put("ClassName", ErrorCode.ALREADY_EXIST.name());
                }
            } else {
                if (clazz != null) {
                    errors.put("ClassName", ErrorCode.ALREADY_EXIST.name());
                }
            }
        }
    }

}
