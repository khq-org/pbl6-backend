package com.backend.pbl6schoolsystem.service.impl;

import com.backend.pbl6schoolsystem.common.CommonUtils;
import com.backend.pbl6schoolsystem.common.constant.ErrorCode;
import com.backend.pbl6schoolsystem.common.enums.Grade;
import com.backend.pbl6schoolsystem.common.exception.NotFoundException;
import com.backend.pbl6schoolsystem.model.dto.common.SchoolYearDTO;
import com.backend.pbl6schoolsystem.model.entity.*;
import com.backend.pbl6schoolsystem.repository.jpa.*;
import com.backend.pbl6schoolsystem.request.schoolyear.CreateUpdateSchoolYearRequest;
import com.backend.pbl6schoolsystem.request.schoolyear.NewSchoolYearRequest;
import com.backend.pbl6schoolsystem.response.ErrorResponse;
import com.backend.pbl6schoolsystem.response.NoContentResponse;
import com.backend.pbl6schoolsystem.response.OnlyIdResponse;
import com.backend.pbl6schoolsystem.response.schoolyear.ListSchoolYearResponse;
import com.backend.pbl6schoolsystem.service.NewSchoolYearService;
import com.backend.pbl6schoolsystem.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewSchoolYearServiceImpl implements NewSchoolYearService {
    private final SchoolYearRepository schoolYearRepository;
    private final ClassRepository classRepository;
    private final StudentClazzRepository studentClazzRepository;
    private final TeacherClassRepository teacherClassRepository;
    private final UserRepository userRepository;
    private final ProfileStudentRepository profileStudentRepository;
    private final LearningResultRepository learningResultRepository;

    @Override
    public ListSchoolYearResponse getListSchoolYear() {
        List<SchoolYearEntity> schoolYears = schoolYearRepository.findAll();
        return ListSchoolYearResponse.builder()
                .setSuccess(true)
                .setItems(!schoolYears.isEmpty()
                        ? schoolYears.stream().map(s -> SchoolYearDTO.builder()
                        .schoolYearId(s.getSchoolYearId())
                        .schoolYear(s.getSchoolYear())
                        .build()).collect(Collectors.toList()) : Collections.emptyList())
                .build();
    }

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
                .setSuccess(true)
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
                .setSuccess(true)
                .setId(schoolYear.getSchoolYearId())
                .setName(schoolYear.getSchoolYear())
                .build();
    }

    @Override
    public NoContentResponse startNewSchoolYear(NewSchoolYearRequest request) {
        Map<String, String> errors = new HashMap<>();
        if (!checkDuplicatedValue(request.getNewClassIds())) { // duplicated
            errors.put("newClassIds", ErrorCode.DUPLICATE_VALUE.name());
        }
        if (!checkDuplicatedValue(request.getTeacherIds())) { // duplicated
            errors.put("teacherIds", ErrorCode.DUPLICATE_VALUE.name());
        }
        if (!errors.isEmpty()) {
            return NoContentResponse.builder()
                    .setSuccess(false)
                    .build();
        }

        List<ClassEntity> oldClassesByIds = classRepository.findClassesByIds(request.getOldClassIds());
        List<ClassEntity> newClassesByIds = classRepository.findClassesByIds(request.getNewClassIds());
        List<UserEntity> teachers = userRepository.findAllById(request.getTeacherIds());
        SchoolYearEntity newSchoolYear = schoolYearRepository.findById(request.getNewSchoolYearId())
                .orElseThrow(() -> new NotFoundException("Not found school year with id " + request.getNewSchoolYearId()));
        ClassEntity oldClazz, newClazz;
        StudentClazzEntity studentClazz;
        TeacherClassEntity teacherClazz;
        LearningResultEntity learningResult;
        List<ProfileStudentEntity> profileStudents;
        List<ClassEntity> saveClasses = new ArrayList<>();
        List<LearningResultEntity> learningResults = new ArrayList<>();
        List<StudentClazzEntity> studentClasses = new ArrayList<>();
        List<TeacherClassEntity> teacherClasses = new ArrayList<>();
        List<ClassEntity> onlyGrade10 = oldClassesByIds.stream()
                .filter(c -> c.getGrade().getGradeId().equals(Grade.GRADE_10.getGradeId()))
                .collect(Collectors.toList());

        for (int i = 0; i < onlyGrade10.size(); i++) {
//            onlyGrade10.get(i).setSubject(""); // FIX ME
//            onlyGrade10.get(i).setIsSpecializedClass(Boolean.FALSE); // FIX ME
            teacherClazz = new TeacherClassEntity();
            teacherClazz.setTeacher(teachers.get(i));
            teacherClazz.setSchoolYear(newSchoolYear);
            teacherClazz.setIsClassLeader(Boolean.TRUE);
            teacherClazz.setClazz(onlyGrade10.get(i));

            teachers.remove(teachers.get(i));
            saveClasses.add(onlyGrade10.get(i));
            teacherClasses.add(teacherClazz);
        }

        for (int i = 0; i < oldClassesByIds.size(); i++) {
            oldClazz = oldClassesByIds.get(i);
            newClazz = newClassesByIds.get(i);
            if (Boolean.TRUE.equals(oldClazz.getIsSpecializedClass())) {
                newClazz.setIsSpecializedClass(Boolean.TRUE);
                newClazz.setSubject(oldClazz.getSubject());
            }
            saveClasses.add(newClazz);

            profileStudents = profileStudentRepository.findByStudentIds(studentClazzRepository.findByClazzIdAndSchoolYearId(oldClazz.getClassId(), request.getOldSchoolYearId())
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
                studentClazz.setSchoolYear(newSchoolYear);
                studentClasses.add(studentClazz);
            }

            teacherClazz = new TeacherClassEntity();
            teacherClazz.setTeacher(teachers.get(i));
            teacherClazz.setClazz(newClazz);
            teacherClazz.setIsClassLeader(Boolean.TRUE);
            teacherClazz.setSchoolYear(newSchoolYear);
            teacherClasses.add(teacherClazz);
        }

        classRepository.saveAll(saveClasses);
        studentClazzRepository.saveAll(studentClasses);
        teacherClassRepository.saveAll(teacherClasses);
        learningResultRepository.saveAll(learningResults);
        return NoContentResponse.builder()
                .setSuccess(true)
                .build();
    }

    public Boolean checkDuplicatedValue(List<Long> ids) {
        Set<Long> setIds = new HashSet<>();
        for (Long id : ids) {
            if (!setIds.add(id)) return false;
        }
        return true;
    }

    public void validRequest(Map<String, String> errors, CreateUpdateSchoolYearRequest request, Long schoolYearId) {
        if (!StringUtils.hasText(request.getSchoolYearName())) {
            errors.put("schoolYear", ErrorCode.MISSING_VALUE.name());
        } else {
            String[] arr = request.getSchoolYearName().split("-");
            int currentYear = LocalDateTime.now().getYear();
            if (!CommonUtils.isNumeric(arr[0]) || !CommonUtils.isNumeric(arr[1])) {
                errors.put("schoolYear", ErrorCode.INVALID_VALUE.name());
            } else {
                if (Integer.parseInt(arr[0]) != currentYear || Integer.parseInt(arr[1]) - Integer.parseInt(arr[0]) != 1) {
                    errors.put("schoolYear", ErrorCode.INVALID_VALUE.name());
                } else {
                    List<SchoolYearEntity> schoolYears = schoolYearRepository.findBySchoolYear(schoolYearId, request.getSchoolYearName());
                    if (!schoolYears.isEmpty()) {
                        errors.put("schoolYear", ErrorCode.ALREADY_EXIST.name());
                    }
                }
            }
        }
    }
}
