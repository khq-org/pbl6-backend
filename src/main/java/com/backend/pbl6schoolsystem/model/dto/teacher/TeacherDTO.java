package com.backend.pbl6schoolsystem.model.dto.teacher;

import com.backend.pbl6schoolsystem.model.dto.common.UserDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"password"})
public class TeacherDTO extends UserDTO {
    private Integer numOfPeriodInWeek;
}
