package com.backend.pbl6schoolsystem.request.leaningresult;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class InputScoreRequest implements Serializable {
    private Long subjectId;
    private Long teacherId;
    private Long semesterId;
    private Long schoolYearId;
    private List<StudentScore> studentScores;

    @Getter
    @Setter
    public static class StudentScore {
        private Long studentId;
        private List<Scores> scores;

        @Getter
        @Setter
        public static class Scores {
            private Double score;
            private String type;
        }
    }
}
