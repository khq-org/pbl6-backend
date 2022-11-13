package com.backend.pbl6schoolsystem.service.impl;

import com.backend.pbl6schoolsystem.common.constant.ErrorCode;
import com.backend.pbl6schoolsystem.common.exception.NotFoundException;
import com.backend.pbl6schoolsystem.model.entity.*;
import com.backend.pbl6schoolsystem.repository.jpa.*;
import com.backend.pbl6schoolsystem.request.schoolyear.CreateUpdateSchoolYearRequest;
import com.backend.pbl6schoolsystem.request.schoolyear.NewSchoolYearRequest;
import com.backend.pbl6schoolsystem.response.ErrorResponse;
import com.backend.pbl6schoolsystem.response.NoContentResponse;
import com.backend.pbl6schoolsystem.response.OnlyIdResponse;
import com.backend.pbl6schoolsystem.service.NewSchoolYearService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewSchoolYearServiceImpl implements NewSchoolYearService {
    private final SchoolYearRepository schoolYearRepository;
    private final ClassRepository classRepository;
    private final StudentClazzRepository studentClazzRepository;
    private final ProfileStudentRepository profileStudentRepository;
    private final LearningResultRepository learningResultRepository;

    @Override
    public OnlyIdResponse createSchoolYear(CreateUpdateSchoolYearRequest request) {
        Map<String, String> errors = new HashMap<>();
        validRequest(errors, request, null);
        if (!errors.isEmpty()) {
            return OnlyIdResponse.builder()
                    .setSuccess(false)
                    .setErrorResponse(ErrorResponse.builder()
                            .setErrors(errors)
                            .build())
                    .build();
        }

        SchoolYearEntity schoolYear = new SchoolYearEntity();
        schoolYear.setSchoolYear(request.getSchoolYearName());
        schoolYear.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        schoolYear = schoolYearRepository.save(schoolYear);

        return OnlyIdResponse.builder()
                .setId(schoolYear.getSchoolYearId())
                .setName(schoolYear.getSchoolYear())
                .build();
    }

    @Override
    public OnlyIdResponse updateSchoolYear(Long schoolYearId, CreateUpdateSchoolYearRequest request) {
        SchoolYearEntity schoolYear = schoolYearRepository.findById(schoolYearId)
                .orElseThrow(() -> new NotFoundException("Not found school year with id " + schoolYearId));
        Map<String, String> errors = new HashMap<>();
        validRequest(errors, request, schoolYearId);
        if (!errors.isEmpty()) {
            return OnlyIdResponse.builder()
                    .setSuccess(false)
                    .setErrorResponse(ErrorResponse.builder()
                            .setErrors(errors)
                            .build())
                    .build();
        }

        schoolYear.setSchoolYear(request.getSchoolYearName());
        schoolYear = schoolYearRepository.save(schoolYear);

        return OnlyIdResponse.builder()
                .setId(schoolYear.getSchoolYearId())
                .setName(schoolYear.getSchoolYear())
                .build();
    }

    @Override
    public NoContentResponse startNewSchoolYear(NewSchoolYearRequest request) {
        List<ClassEntity> oldClassesByIds = classRepository.findClassesByIds(request.getOldClassIds());
        List<ClassEntity> newClassesByIds = classRepository.findClassesByIds(request.getNewClassIds());
        SchoolYearEntity newSchoolYear = schoolYearRepository.findById(request.getNewSchoolYearId())
                .orElseThrow(() -> new NotFoundException("Not found school year with id " + request.getNewSchoolYearId()));
        ClassEntity oldClazz, newClazz;
        StudentClazzEntity studentClazz;
        LearningResultEntity learningResult;
        List<ProfileStudentEntity> profileStudents;
        List<ClassEntity> saveClasses = new ArrayList<>();
        List<LearningResultEntity> learningResults = new ArrayList<>();
        List<StudentClazzEntity> studentClasses = new ArrayList<>();

        for (int i = 0; i < oldClassesByIds.size(); i++) {
            oldClazz = oldClassesByIds.get(i);
            newClazz = newClassesByIds.get(i);
            if (Boolean.TRUE.equals(oldClazz.getIsSpecializedClass())) {
                newClazz.setIsSpecializedClass(Boolean.TRUE);
                newClazz.setSubject(oldClazz.getSubject());
            }
            saveClasses.add(newClazz);

            profileStudents = profileStudentRepository.findByStudentIds(studentClazzRepository.findByClazzId(oldClazz.getClassId())
                    .stream().map(s -> s.getStudent().getUserId()).collect(Collectors.toList()));
            for (ProfileStudentEntity profileStudent : profileStudents) {
                learningResult = new LearningResultEntity();
                learningResult.setProfileStudent(profileStudent);
                learningResult.setClazz(newClazz);
                learningResult.setSchoolYear(newSchoolYear);
                learningResults.add(learningResult);

                studentClazz = new StudentClazzEntity();
                studentClazz.setClazz(newClazz);
                studentClazz.setStudent(profileStudent.getStudent());
                studentClasses.add(studentClazz);
            }
        }

        classRepository.saveAll(saveClasses);
        studentClazzRepository.saveAll(studentClasses);
        learningResultRepository.saveAll(learningResults);
        return NoContentResponse.builder()
                .setSuccess(true)
                .build();
    }

    public void validRequest(Map<String, String> errors, CreateUpdateSchoolYearRequest request, Long schoolYearId) {
        if (!StringUtils.hasText(request.getSchoolYearName())) {
            errors.put("schoolYear", ErrorCode.MISSING_VALUE.name());
        } else {
            List<SchoolYearEntity> schoolYears = schoolYearRepository.findBySchoolYear(schoolYearId, request.getSchoolYearName());
            if (!schoolYears.isEmpty()) {
                errors.put("schoolYear", ErrorCode.ALREADY_EXIST.name());
            }
        }
    }
}
