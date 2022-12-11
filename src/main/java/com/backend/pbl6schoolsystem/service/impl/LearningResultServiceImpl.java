package com.backend.pbl6schoolsystem.service.impl;

import com.backend.pbl6schoolsystem.common.constant.ErrorCode;
import com.backend.pbl6schoolsystem.common.enums.ExamType;
import com.backend.pbl6schoolsystem.common.enums.Semester;
import com.backend.pbl6schoolsystem.common.exception.NotFoundException;
import com.backend.pbl6schoolsystem.model.dto.clazz.ExamResultClassDTO;
import com.backend.pbl6schoolsystem.model.dto.common.SchoolYearDTO;
import com.backend.pbl6schoolsystem.model.dto.common.SemesterDTO;
import com.backend.pbl6schoolsystem.model.dto.common.SubjectDTO;
import com.backend.pbl6schoolsystem.model.dto.student.ExamResultDTO;
import com.backend.pbl6schoolsystem.model.dto.student.LearningResultDTO;
import com.backend.pbl6schoolsystem.model.dto.student.LearningResultDetailDTO;
import com.backend.pbl6schoolsystem.model.entity.*;
import com.backend.pbl6schoolsystem.repository.dsl.ExamResultDslRepository;
import com.backend.pbl6schoolsystem.repository.jpa.*;
import com.backend.pbl6schoolsystem.request.leaningresult.InputScoreRequest;
import com.backend.pbl6schoolsystem.request.leaningresult.LoadExamResultClassRequest;
import com.backend.pbl6schoolsystem.request.leaningresult.LoadExamResultStudentRequest;
import com.backend.pbl6schoolsystem.request.leaningresult.ModifyScoreRequest;
import com.backend.pbl6schoolsystem.response.ErrorResponse;
import com.backend.pbl6schoolsystem.response.NoContentResponse;
import com.backend.pbl6schoolsystem.response.learningresult.LearningResultDetailResponse;
import com.backend.pbl6schoolsystem.response.learningresult.LoadExamResultClassResponse;
import com.backend.pbl6schoolsystem.response.learningresult.LoadExamResultResponse;
import com.backend.pbl6schoolsystem.security.UserPrincipal;
import com.backend.pbl6schoolsystem.service.LearningResultService;
import com.backend.pbl6schoolsystem.util.RequestUtil;
import com.backend.pbl6schoolsystem.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LearningResultServiceImpl implements LearningResultService {
    private final UserRepository userRepository;
    private final LearningResultRepository learningResultRepository;
    private final ClassRepository classRepository;
    private final SchoolYearRepository schoolYearRepository;
    private final SemesterRepository semesterRepository;
    private final SubjectRepository subjectRepository;
    private final ExamResultRepository examResultRepository;
    private final TeacherClassRepository teacherClassRepository;
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

    // for student
    @Override
    public LoadExamResultResponse loadExamResult(LoadExamResultStudentRequest request) {
        UserPrincipal principal = SecurityUtils.getPrincipal();
        Map<String, String> errors = new HashMap<>();

        if (request.getSchoolYearId() == null) {
            errors.put("schoolYearId", ErrorCode.MISSING_VALUE.name());
        }
        if (request.getStudentId() == null) {
            errors.put("studentId", ErrorCode.MISSING_VALUE.name());
        }
        if (principal.isSchoolAdmin()) {
            if (request.getSubjectId() == null) {
                errors.put("subjectId", ErrorCode.MISSING_VALUE.name());
            }
        }

        if (!errors.isEmpty()) {
            return LoadExamResultResponse.builder()
                    .setSuccess(false)
                    .setErrorResponse(ErrorResponse.builder()
                            .setErrors(errors)
                            .build())
                    .build();
        }

        if (principal.isTeacher()) {
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
                                .subject(SubjectDTO.builder()
                                        .setSubjectId(e.getSubject().getSubjectId())
                                        .setSubject(e.getSubject().getSubject())
                                        .build())
                                .semester(SemesterDTO.builder()
                                        .semesterId(e.getSemester().getSemesterId())
                                        .semester(e.getSemester().getSemester())
                                        .build())
                                .schoolYear(SchoolYearDTO.builder()
                                        .schoolYearId(e.getSchoolYear().getSchoolYearId())
                                        .schoolYear(e.getSchoolYear().getSchoolYear())
                                        .build())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    // for class
    @Override
    public LoadExamResultClassResponse loadExamResultClass(LoadExamResultClassRequest request) {
        Map<String, String> errors = new HashMap<>();

        if (request.getClassId() == null) {
            errors.put("classId", ErrorCode.MISSING_VALUE.name());
        }
        if (request.getSchoolYearId() == null) {
            errors.put("schoolYearId", ErrorCode.MISSING_VALUE.name());
        }

        UserPrincipal principal = SecurityUtils.getPrincipal();
        if (principal.isSchoolAdmin()) {
            if (request.getSubjectId() == null) {
                errors.put("subjectId", ErrorCode.MISSING_VALUE.name());
            }
        }

        if (!errors.isEmpty()) {
            return LoadExamResultClassResponse.builder()
                    .setSuccess(false)
                    .setErrorResponse(ErrorResponse.builder()
                            .setErrors(errors)
                            .build())
                    .build();
        }

        if (principal.isTeacher()) {
            request.setSubjectId(userRepository.findById(principal.getUserId())
                    .get().getSubject().getSubjectId());
        }

        SubjectEntity subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new NotFoundException("Not found subject"));
        ClassEntity clazz = classRepository.findById(request.getClassId())
                .orElseThrow(() -> new NotFoundException("Not found clazz"));

        ExamResultClassDTO.ExamResultClassDTOBuilder builder = ExamResultClassDTO.builder();
        builder.setSubject(ExamResultClassDTO.Subject.builder()
                .setSubjectId(subject.getSubjectId())
                .setSubject(subject.getSubject())
                .build());
        builder.setClazz(ExamResultClassDTO.Clazz.builder()
                .setClassId(clazz.getClassId())
                .setClassName(clazz.getClazz())
                .build());

        List<UserEntity> studentsInClass = userRepository.findByClassAndSchoolYear(request.getClassId(), request.getSchoolYearId());
        List<ExamResultEntity> examResults;

        List<ExamResultClassDTO.ExamResult> examResultsBuilder = new ArrayList<>();
        for (UserEntity student : studentsInClass) {
            examResults = examResultDslRepository.listExamResult(LoadExamResultStudentRequest.builder()
                    .setStudentId(student.getUserId())
                    .setSchoolYearId(request.getSchoolYearId())
                    .setSemesterId(RequestUtil.defaultIfNull(request.getSemesterId(), -1L))
                    .setSubjectId(request.getSubjectId())
                    .build());
            examResultsBuilder.add(ExamResultClassDTO.ExamResult.builder()
                    .setStudent(ExamResultClassDTO.ExamResult.Student.builder()
                            .setStudentId(student.getUserId())
                            .setFirstName(student.getFirstName())
                            .setLastName(student.getLastName())
                            .build())
                    .setScores(examResults.stream()
                            .map(er -> ExamResultClassDTO.ExamResult.Score.builder()
                                    .setId(er.getExamResultId())
                                    .setScore(er.getScore())
                                    .setType(er.getExamType())
                                    .build())
                            .collect(Collectors.toList()))
                    .build());
        }
        builder.setExamResults(examResultsBuilder);

        return LoadExamResultClassResponse.builder()
                .setSuccess(true)
                .setExamResultClass(builder.build())
                .build();
    }

    @Override
    public NoContentResponse inputScore(InputScoreRequest request) {
        UserPrincipal principal = SecurityUtils.getPrincipal();
        Map<String, String> errors = new HashMap<>();

        if (principal.isSchoolAdmin()) {
            if (request.getTeacherId() == null) {
                errors.put("teacherId", ErrorCode.MISSING_VALUE.name());
            }
            if (request.getSubjectId() == null) {
                errors.put("subjectId", ErrorCode.MISSING_VALUE.name());
            }
        }
        if (request.getSemesterId() == null) {
            errors.put("semesterId", ErrorCode.MISSING_VALUE.name());
        }
        if (request.getSchoolYearId() == null) {
            errors.put("schoolYearId", ErrorCode.MISSING_VALUE.name());
        }

        if (!errors.isEmpty()) {
            return NoContentResponse.builder()
                    .setSuccess(false)
                    .setErrorResponse(ErrorResponse.builder()
                            .setErrors(errors)
                            .build())
                    .build();
        }

        if (principal.isTeacher()) {
            request.setTeacherId(principal.getUserId());
            request.setSubjectId(userRepository.findById(principal.getUserId())
                    .get().getSubject().getSubjectId());
        }

        UserEntity user = userRepository.findById(principal.getUserId()).get();
        SubjectEntity subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new NotFoundException("Not found subject"));
        SchoolYearEntity schoolYear = schoolYearRepository.findById(request.getSchoolYearId())
                .orElseThrow(() -> new NotFoundException("Not found school year"));
        SemesterEntity semester = semesterRepository.findById(request.getSemesterId())
                .orElseThrow(() -> new NotFoundException("Not found semester"));
        List<InputScoreRequest.StudentScore> studentScores = request.getStudentScores();

        if (!CollectionUtils.isEmpty(studentScores)) {
            List<ExamResultEntity> examResults = new ArrayList<>();
            ExamResultEntity examResult;
            List<Long> studentIds = studentScores.stream().map(sc -> sc.getStudentId()).collect(Collectors.toList());
            List<LearningResultEntity> learningResults = learningResultRepository.findByStudentIdsAndSchoolYearId(studentIds, request.getSchoolYearId());
            for (int i = 0; i < studentScores.size(); i++) {
                List<InputScoreRequest.StudentScore.Scores> scores = studentScores.get(i).getScores();
                for (InputScoreRequest.StudentScore.Scores score : scores) {
                    examResult = new ExamResultEntity();
                    examResult.setStudent(learningResults.get(i).getProfileStudent().getStudent());
                    examResult.setLearningResult(learningResults.get(i));
                    examResult.setSemester(semester);
                    examResult.setSchoolYear(schoolYear);
                    examResult.setSubject(subject);
                    examResult.setScore(score.getScore());
                    examResult.setExamType(score.getType());
                    examResult.setCreatedBy(user);
                    examResult.setCreatedDate(new Timestamp(System.currentTimeMillis()));
                    examResults.add(examResult);
                }
            }
            examResultRepository.saveAll(examResults);
        }

        return NoContentResponse.builder()
                .setSuccess(true)
                .build();
    }

    @Override
    public NoContentResponse modifyScore(ModifyScoreRequest request) {
        return null;
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
