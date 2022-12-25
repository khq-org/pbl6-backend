package com.backend.pbl6schoolsystem.model.dto.clazz;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder(setterPrefix = "set")
public class ClassLearningResultDTO implements Serializable {
    private Long classId;
    private String className;
    private String schoolYear;
    private List<StudentLearningResult> studentLearningResults;
    private Boolean isAllowUpdate;

    @Getter
    @Setter
    @Builder(setterPrefix = "set")
    public static class StudentLearningResult implements Serializable {
        private Long studentId;
        private List<Long> arrAvgSubjectScore;
        private Double avgSemesterI;
        private Double avgSemesterII;
        private Double avgSchoolYear;
        private String learningGrade;
        private String conduct;
    }
}
