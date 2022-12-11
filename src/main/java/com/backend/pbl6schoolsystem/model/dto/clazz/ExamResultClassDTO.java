package com.backend.pbl6schoolsystem.model.dto.clazz;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder(setterPrefix = "set")
public class ExamResultClassDTO implements Serializable {
    private Clazz clazz;
    private Subject subject;
    private List<ExamResult> examResults;

    @Getter
    @Setter
    @Builder(setterPrefix = "set")
    public static class Clazz {
        private Long classId;
        private String className;
    }

    @Getter
    @Setter
    @Builder(setterPrefix = "set")
    public static class Subject {
        private Long subjectId;
        private String subject;
    }

    @Getter
    @Setter
    @Builder(setterPrefix = "set")
    public static class ExamResult {
        private Student student;
        private List<Score> scores;

        @Getter
        @Setter
        @Builder(setterPrefix = "set")
        public static class Student {
            private Long studentId;
            private String lastName;
            private String firstName;
        }

        @Getter
        @Setter
        @Builder(setterPrefix = "set")
        public static class Score {
            private Long id;
            private Double score;
            private String type;
        }
    }
}
