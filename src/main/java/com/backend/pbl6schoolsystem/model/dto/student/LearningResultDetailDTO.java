package com.backend.pbl6schoolsystem.model.dto.student;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder(setterPrefix = "set")
public class LearningResultDetailDTO implements Serializable {
    private LearningResultDTO learningResult;
    private List<StudyScore> studyScores;

    @Getter
    @Setter
    @Builder(setterPrefix = "set")
    public static class StudyScore {
        private Subject subject;
        private List<SemesterScore> semesterScores;

        @Getter
        @Setter
        @Builder(setterPrefix = "set")
        public static class Subject {
            private Long subjectId;
            private String subjectName;
        }

        @Getter
        @Setter
        @Builder(setterPrefix = "set")
        public static class SemesterScore {
            private String semester;
            private List<Score> scores;
            private Double avgScore;

            @Getter
            @Setter
            @Builder(setterPrefix = "set")
            public static class Score {
                private Double score;
                private String type;
            }
        }
    }
}

