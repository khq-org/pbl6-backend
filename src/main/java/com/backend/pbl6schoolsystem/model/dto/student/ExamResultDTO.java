package com.backend.pbl6schoolsystem.model.dto.student;

import com.backend.pbl6schoolsystem.model.dto.common.SchoolYearDTO;
import com.backend.pbl6schoolsystem.model.dto.common.SemesterDTO;
import com.backend.pbl6schoolsystem.model.dto.common.SubjectDTO;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamResultDTO implements Serializable {
    private Long examResultId;
    private SubjectDTO subject;
    private String examType;
    private Double score;
    private SemesterDTO semester;
    private SchoolYearDTO schoolYear;
}
