package com.backend.pbl6schoolsystem.model.dto.student;

import com.backend.pbl6schoolsystem.model.dto.common.SchoolYearDTO;
import com.backend.pbl6schoolsystem.model.dto.common.SemesterDTO;
import lombok.*;

import java.io.Serializable;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamResultDTO implements Serializable {
    private Long examResultId;
    private Long subjectId;
    private String subjectName;
    private Long studentId;
    private String examType;
    private Double score;
    private SemesterDTO semester;
    private SchoolYearDTO schoolYear;
}
