package com.backend.pbl6schoolsystem.request.leaningresult;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class UpdateClassLearningResult implements Serializable {
    private List<StudentLearningResult> studentLearningResults;

    @Getter
    @Setter
    public static class StudentLearningResult {
        private Long learningResultId;
        private Double avgScore;
        private String conduct;
        private String learningGrade;
        private Boolean isPassed;
    }
}
