package com.backend.pbl6schoolsystem.response.learningresult;

import com.backend.pbl6schoolsystem.response.ErrorResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder(setterPrefix = "set")
public class GetClassLearningResultResponse {
    private Boolean success;
    private ErrorResponse errorResponse;
    private Long classId;
    private String className;
    private String schoolYear;
    private List<StudentLearningResult> studentLearningResults;
    private Boolean isAllowUpdate;

    @Getter
    @Setter
    @Builder(setterPrefix = "set")
    public static class StudentLearningResult {
        private Long learningResultId;
        private Long studentId;
        private List<Long> arrAvgSubjectScore;
        private Double avgSemesterI;
        private Double avgSemesterII;
        private Double avgSchoolYear;
        private String learningGrade;
        private String conduct;
    }

}
