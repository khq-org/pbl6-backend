package com.backend.pbl6schoolsystem.model.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClazzDTO implements Serializable {
    private Long classId;
    private String clazz;
    private GradeDTO grade;
    @JsonIgnoreProperties({"password", "schoolId", "districtId"})
    private UserDTO teacher;
    private Boolean specializedClass;
}
