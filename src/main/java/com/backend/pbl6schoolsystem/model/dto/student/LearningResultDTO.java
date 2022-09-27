package com.backend.pbl6schoolsystem.model.dto.student;

import com.backend.pbl6schoolsystem.model.dto.common.SchoolYearDTO;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LearningResultDTO implements Serializable {
    private Long learningResultId;
    private SchoolYearDTO schoolYear;
    private Double averageScore;
    private String conduct;
    private String learningGrade;
    private Boolean isPassed;
}
