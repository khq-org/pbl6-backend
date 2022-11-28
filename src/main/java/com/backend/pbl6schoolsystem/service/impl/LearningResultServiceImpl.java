package com.backend.pbl6schoolsystem.service.impl;

import com.backend.pbl6schoolsystem.common.constant.ErrorCode;
import com.backend.pbl6schoolsystem.common.enums.ExamType;
import com.backend.pbl6schoolsystem.common.enums.Semester;
import com.backend.pbl6schoolsystem.common.exception.NotFoundException;
import com.backend.pbl6schoolsystem.model.dto.common.SubjectDTO;
import com.backend.pbl6schoolsystem.model.dto.student.ExamResultDTO;
import com.backend.pbl6schoolsystem.model.dto.student.LearningResultDTO;
import com.backend.pbl6schoolsystem.model.dto.student.LearningResultDetailDTO;
import com.backend.pbl6schoolsystem.model.entity.ExamResultEntity;
import com.backend.pbl6schoolsystem.model.entity.LearningResultEntity;
import com.backend.pbl6schoolsystem.model.entity.SubjectEntity;
import com.backend.pbl6schoolsystem.model.entity.UserEntity;
import com.backend.pbl6schoolsystem.repository.dsl.ExamResultDslRepository;
import com.backend.pbl6schoolsystem.repository.jpa.ExamResultRepository;
import com.backend.pbl6schoolsystem.repository.jpa.LearningResultRepository;
import com.backend.pbl6schoolsystem.repository.jpa.SubjectRepository;
import com.backend.pbl6schoolsystem.repository.jpa.UserRepository;
import com.backend.pbl6schoolsystem.request.leaningresult.LoadExamResultRequest;
import com.backend.pbl6schoolsystem.response.learningresult.LearningResultDetailResponse;
import com.backend.pbl6schoolsystem.response.learningresult.LoadExamResultResponse;
import com.backend.pbl6schoolsystem.security.UserPrincipal;
import com.backend.pbl6schoolsystem.service.LearningResultService;
import com.backend.pbl6schoolsystem.util.RequestUtil;
import com.backend.pbl6schoolsystem.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LearningResultServiceImpl implements LearningResultService {
    private final UserRepository userRepository;
    private final LearningResultRepository learningResultRepository;
    private final SubjectRepository subjectRepository;
    private final ExamResultRepository examResultRepository;
    private final ExamResultDslRepository examResultDslRepository;

    @Override
    public LearningResultDetailResponse getLearningResultDetail(Long learningResultId) {
        // get learning result
        LearningResultEntity learningResult = learningResultRepository.findByLearningResult(learningResultId)
                .orElseThrow(() -> new NotFoundException("Not found learning result with id " + learningResultId));

        // get exam result
        List<SubjectEntity> subjects = subjectRepository.findAll();
        subjects.sort(Comparator.comparing(SubjectEntity::getSubject));
        Map<SubjectEntity/*subject*/, List<ExamResultEntity>/*exams*/> mapStudyScore = new LinkedHashMap<>();
        List<ExamResultEntity> examResults;
        for (SubjectEntity subject : subjects) {
            examResults = examResultRepository.listExamResult(learningResultId, subject.getSubjectId());
            mapStudyScore.put(subject, examResults);
        }

        return LearningResultDetailResponse.builder()
                .setSuccess(true)
                .setLearningResultDetail(LearningResultDetailDTO.builder()
                        .setLearningResult(LearningResultDTO.builder()
                                .setLearningResultId(learningResult.getLearningResultId())
                                .setSchoolYear(learningResult.getSchoolYear().getSchoolYear())
                                .setClassName(learningResult.getClazz().getClazz())
                                .setAverageScore(RequestUtil.defaultIfNull(learningResult.getAverageScore(), 0D))
                                .setConduct(RequestUtil.blankIfNull(learningResult.getConduct()))
                                .setIsPassed(Boolean.TRUE.equals(learningResult.getIsPassed()) ? Boolean.TRUE : Boolean.FALSE)
                                .build())
                        .setStudyScores(mapStudyScore.keySet().stream()
                                .map(s -> LearningResultDetailDTO.StudyScore.builder()
                                        .setSubject(SubjectDTO.builder()
                                                .setSubjectId(RequestUtil.defaultIfNull(s.getSubjectId(), -1L))
                                                .setSubject(RequestUtil.blankIfNull(s.getSubject()))
                                                .build())
                                        .setSemesters(List.of(Semester.SEMESTER_I, Semester.SEMESTER_II).stream()
                                                .map(se -> LearningResultDetailDTO.StudyScore.SemesterDetail.builder()
                                                        .setSemester(se.getName())
                                                        .setExams(List.of(ExamType.TYPE_I, ExamType.TYPE_II, ExamType.TYPE_III, ExamType.TYPE_IV)
                                                                .stream().map(et -> LearningResultDetailDTO.StudyScore.SemesterDetail.Exam.builder()
                                                                        .setExam(et.getName())
                                                                        .setScores(!mapStudyScore.get(s).isEmpty() ? mapStudyScore.get(s).stream()
                                                                                .filter(e -> e.getExamType().equalsIgnoreCase(et.getName())
                                                                                        && e.getSemester().getSemester().equalsIgnoreCase(se.getName()))
                                                                                .map(sc -> sc.getScore())
                                                                                .collect(Collectors.toList()) : Collections.emptyList())
                                                                        .build())
                                                                .collect(Collectors.toList()))
                                                        .setAverageScore(calculateAvg(mapStudyScore.get(s), se.getId()))
                                                        .build())
                                                .collect(Collectors.toList()))
                                        .setAverageScore((calculateAvg(mapStudyScore.get(s), Semester.SEMESTER_I.getId())
                                                + calculateAvg(mapStudyScore.get(s), Semester.SEMESTER_II.getId()) * 2) / 3)
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .build();
    }

    @Override
    public LoadExamResultResponse loadExamResult(LoadExamResultRequest request) {
        UserPrincipal principal = SecurityUtils.getPrincipal();
        Map<String, String> errors = new HashMap<>();
        if (principal.isSchoolAdmin()) {
            if (request.getSubjectId() == null) {
                errors.put("subjectId", ErrorCode.MISSING_VALUE.name());
            }
        }
        else if (principal.isTeacher()) {
            request.setSubjectId(userRepository.findById(principal.getUserId())
                    .get().getSubject().getSubjectId());
        }
        List<ExamResultEntity> examResults = examResultDslRepository.listExamResult(request);
        return LoadExamResultResponse.builder()
                .setSuccess(true)
                .setExamResult(examResults.stream()
                        .map(e -> ExamResultDTO.builder()
                                .examResultId(e.getExamResultId())
                                .examType(e.getExamType())
                                .score(e.getScore())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    // calculate average
    private double calculateAvg(List<ExamResultEntity> examResults, Long semesterId) {
        examResults = examResults.stream()
                .filter(e -> e.getSemester().getSemesterId().equals(semesterId))
                .collect(Collectors.toList());
        int index = 0;
        double avg = 0.0;
        for (ExamResultEntity examResult : examResults) {
            if (List.of(ExamType.TYPE_I.getName(), ExamType.TYPE_II.getName()).contains(examResult.getExamType())) {
                avg += examResult.getScore();
                index += 1;
            } else if (ExamType.TYPE_III.getName().equals(examResult.getExamType())) {
                avg += examResult.getScore() * 2;
                index += 2;
            } else {
                avg += examResult.getScore() * 3;
                index += 3;
            }
        }
        return index != 0 ? avg / index : 0.0;
    }
}
