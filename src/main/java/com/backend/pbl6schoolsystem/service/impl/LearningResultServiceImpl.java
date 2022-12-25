package com.backend.pbl6schoolsystem.service.impl;

import com.backend.pbl6schoolsystem.common.constant.ErrorCode;
import com.backend.pbl6schoolsystem.common.enums.ExamType;
import com.backend.pbl6schoolsystem.common.enums.Semester;
import com.backend.pbl6schoolsystem.common.enums.Subject;
import com.backend.pbl6schoolsystem.common.exception.NotFoundException;
import com.backend.pbl6schoolsystem.model.dto.clazz.ClassLearningResultDTO;
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
import com.backend.pbl6schoolsystem.response.ErrorResponse;
import com.backend.pbl6schoolsystem.response.NoContentResponse;
import com.backend.pbl6schoolsystem.response.learningresult.GetClassLearningResultResponse;
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
    private final ExamResultDslRepository examResultDslRepository;
    private final StudentClazzRepository studentClazzRepository;
    private final TeacherClassRepository teacherClassRepository;

    @Override
    public LearningResultDetailResponse getLearningResultDetail(Long learningResultId) {
        // get learning result
        LearningResultEntity learningResult = learningResultRepository.findByLearningResult(learningResultId)
                .orElseThrow(() -> new NotFoundException("Not found learning result with id " + learningResultId));

        // get exam result
        List<SubjectEntity> subjects = subjectRepository.findAll();
        subjects.sort(Comparator.comparing(SubjectEntity::getSubject));
        List<LearningResultDetailDTO.StudyScore> studyScores = new ArrayList<>();
        List<ExamResultEntity> examResults = examResultRepository.listExamResult(learningResultId, subjects.stream()
                .map(SubjectEntity::getSubjectId)
                .collect(Collectors.toList()));
        for (SubjectEntity subject : subjects) {
            List<LearningResultDetailDTO.StudyScore.SemesterScore> semesterScores = new ArrayList<>();
            List.of(Semester.SEMESTER_I, Semester.SEMESTER_II).forEach(s -> {
                List<ExamResultEntity> scores = examResults.stream().filter(er -> er.getSemester().getSemesterId().equals(s.getId())
                        && er.getSubject().getSubjectId().equals(subject.getSubjectId())).collect(Collectors.toList());
                semesterScores.add(LearningResultDetailDTO.StudyScore.SemesterScore.builder()
                        .setSemester(s.getName())
                        .setScores(scores.stream()
                                .map(sc -> LearningResultDetailDTO.StudyScore.SemesterScore.Score.builder()
                                        .setScore(sc.getScore())
                                        .setType(sc.getExamType())
                                        .build())
                                .collect(Collectors.toList()))
                        .setAvgScore(calculateAvg(scores))
                        .build());
            });

            Double avgSubjectAllSemester = 0D;
            for (LearningResultDetailDTO.StudyScore.SemesterScore semesterScore : semesterScores) {
                avgSubjectAllSemester += (semesterScore.getSemester().equals(Semester.SEMESTER_I.getName())) ?
                        semesterScore.getAvgScore() : 2 * semesterScore.getAvgScore();
            }

            studyScores.add(LearningResultDetailDTO.StudyScore.builder()
                    .setSubject(LearningResultDetailDTO.StudyScore.Subject.builder()
                            .setSubjectId(subject.getSubjectId())
                            .setSubjectName(subject.getSubject())
                            .build())
                    .setSemesterScores(semesterScores)
                    .setAvgScore(avgSubjectAllSemester)
                    .build());
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
                        .setStudyScores(studyScores)
                        .setAvgScore(calculateSchoolYearAvg(studyScores))
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
    public GetClassLearningResultResponse getClassLearningResult(Long classId, Long schoolYearId) {
        GetClassLearningResultResponse.GetClassLearningResultResponseBuilder builder = GetClassLearningResultResponse.builder();
        Map<String, String> errors = new HashMap<>();
        if (classId == null) {
            errors.put("classId", ErrorCode.MISSING_VALUE.name());
        }
        if (schoolYearId == null) {
            errors.put("schoolYearId", ErrorCode.MISSING_VALUE.name());
        }

        UserPrincipal principal = SecurityUtils.getPrincipal();
        if (principal.isTeacher()) {
            Optional<TeacherClassEntity> teacherClass = teacherClassRepository.findByTeacherIfLeaderAndClassIdAndSchoolYearId(principal.getUserId(), classId, schoolYearId);
            if (teacherClass.isEmpty()) {
                errors.put("classId or schoolYearId", ErrorCode.INVALID_VALUE.name());
            }
        }

        if (!errors.isEmpty()) {
            return builder.setSuccess(false)
                    .setErrorResponse(ErrorResponse.builder()
                            .setErrors(errors)
                            .build())
                    .build();
        }

        ClassEntity clazz = classRepository.findById(classId).orElseThrow(() -> new NotFoundException("Not found class"));
        SchoolYearEntity schoolYear = schoolYearRepository.findById(schoolYearId).orElseThrow(() -> new NotFoundException("Not found schoolYear"));
        ClassLearningResultDTO.ClassLearningResultDTOBuilder classLearningResultDTOBuilder = ClassLearningResultDTO.builder();
        classLearningResultDTOBuilder.setClassId(classId)
                .setClassName(clazz.getClazz())
                .setSchoolYear(schoolYear.getSchoolYear());
        List<ClassLearningResultDTO.StudentLearningResult> studentLearningResults = new ArrayList<>();
        // get all subject
        List<SubjectEntity> subjects = subjectRepository.findAll();
        // get all student by class, schoolYear
        List<UserEntity> students = studentClazzRepository.findByClazzIdAndSchoolYearId(classId, schoolYearId)
                .stream().map(StudentClazzEntity::getStudent).collect(Collectors.toList());
        List<ExamResultEntity> examResults = examResultRepository.listExamResultByLearningResults(
                learningResultRepository.findByStudentIdsAndSchoolYearId(students.stream()
                                .map(s -> s.getUserId()).collect(Collectors.toList()), schoolYearId).stream()
                        .map(er -> er.getLearningResultId()).collect(Collectors.toList()));
        students.forEach(student -> {
            List<Long> avgSubjectScore = new ArrayList<>();
            subjects.forEach(subject -> {
                // filter examResult by student and subject
                List<ExamResultEntity> ers = examResults.stream().filter(er -> er.getStudent().getStudentId().equals(student.getUserId())
                        && er.getSubject().getSubjectId().equals(subject.getSubjectId())).collect(Collectors.toList());
            });
            studentLearningResults.add(ClassLearningResultDTO.StudentLearningResult.builder()
                    .setStudentId(student.getUserId())
                    .setLearningGrade(null)
                    .setArrAvgSubjectScore(avgSubjectScore)
                    .setAvgSemesterI(null)
                    .setAvgSemesterII(null)
                    .build());
        });

        return builder
                .setClassLearningResult(classLearningResultDTOBuilder
                        .setStudentLearningResults(studentLearningResults)
                        .build())
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
            List<ExamResultEntity> examResultsForRemove = new ArrayList<>();
            ExamResultEntity examResult;
            List<Long> studentIds = studentScores.stream().map(sc -> sc.getStudentId()).collect(Collectors.toList());
            List<LearningResultEntity> learningResults = learningResultRepository.findByStudentIdsAndSchoolYearId(studentIds, request.getSchoolYearId());
            Optional<ExamResultEntity> examResultFromDB;
            List<UserEntity> students = userRepository.findAllById(studentIds);
            for (int i = 0; i < students.size(); i++) {
                List<InputScoreRequest.StudentScore.Scores> scores = studentScores.get(i).getScores();
                for (InputScoreRequest.StudentScore.Scores score : scores) {
                    examResultFromDB = examResultRepository.findFromDB(subject.getSubjectId(), schoolYear.getSchoolYearId(), semester.getSemesterId(),
                            students.get(i).getUserId(), score.getType());
                    if (examResultFromDB.isPresent()) {
                        examResult = examResultFromDB.get();
                        if (score.getScore() != null) {
                            examResult.setScore(score.getScore());
                            examResult.setModifiedDate(new Timestamp(System.currentTimeMillis()));
                            examResult.setModifiedBy(user);
                        } else {
                            examResultsForRemove.add(examResult);
                            continue;
                        }
                    } else {
                        examResult = new ExamResultEntity();
                        examResult.setStudent(students.get(i));
                        examResult.setLearningResult(learningResults.get(i));
                        examResult.setSemester(semester);
                        examResult.setSchoolYear(schoolYear);
                        examResult.setSubject(subject);
                        examResult.setScore(score.getScore());
                        examResult.setExamType(score.getType());
                        examResult.setCreatedBy(user);
                        examResult.setCreatedDate(new Timestamp(System.currentTimeMillis()));
                    }
                    examResults.add(examResult);
                }
            }
            examResultRepository.saveAll(examResults);
            examResultRepository.deleteAll(examResultsForRemove);
        }

        return NoContentResponse.builder()
                .setSuccess(true)
                .build();
    }

    // calculate average
    private double calculateAvg(List<ExamResultEntity> examResults) {
        int index = 0;
        double avg = 0.0;
        for (ExamResultEntity examResult : examResults) {
            if (List.of(ExamType.A1.getName(), ExamType.A2.getName(), ExamType.A3.getName(), ExamType.B1.getName(),
                    ExamType.B2.getName(), ExamType.B3.getName(), ExamType.B4.getName()).contains(examResult.getExamType())) {
                avg += examResult.getScore();
                index += 1;
            } else if (List.of(ExamType.C1.getName(), ExamType.D1.getName(), ExamType.D2.getName(), ExamType.D3.getName(),
                    ExamType.D4.getName()).equals(examResult.getExamType())) {
                avg += examResult.getScore() * 2;
                index += 2;
            } else {
                avg += examResult.getScore() * 3;
                index += 3;
            }
        }
        return index != 0 ? avg / index : 0.0;
    }

    private double calculateSchoolYearAvg(List<LearningResultDetailDTO.StudyScore> studyScores) {
        double avg = 0.0;
        for (LearningResultDetailDTO.StudyScore studyScore : studyScores) {
            if (List.of(Subject.MATHS, Subject.LITERATURE).contains(studyScore.getSubject().getSubjectName())) {
                avg += studyScore.getAvgScore() * 2;
            } else {
                avg += studyScore.getAvgScore();
            }
        }
        return avg / 15.0;
    }

}
