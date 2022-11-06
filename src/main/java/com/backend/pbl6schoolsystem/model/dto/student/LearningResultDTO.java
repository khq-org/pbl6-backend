package com.backend.pbl6schoolsystem.model.dto.student;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "set")
public class LearningResultDTO implements Serializable {
    private Long learningResultId;
    private String schoolYear;
    private String className;
    private Double averageScore;
    private String conduct;
    private String learningGrade;
    private Boolean isPassed;
}
