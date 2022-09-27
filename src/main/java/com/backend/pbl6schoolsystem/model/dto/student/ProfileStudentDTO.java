package com.backend.pbl6schoolsystem.model.dto.student;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"clazz"})
public class ProfileStudentDTO extends StudentDetailDTO {
    private Long profileStudentId;
    private List<LearningResultDTO> learningResults;
}
