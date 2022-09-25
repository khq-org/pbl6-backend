package com.backend.pbl6schoolsystem.model.dto.student;

import com.backend.pbl6schoolsystem.model.dto.common.ClazzDTO;
import com.backend.pbl6schoolsystem.model.dto.common.UserDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"password", "workingPosition", "positionGroup", "recruitmentDay", "factorSalary", "rank", "level"})
public class StudentDetailDTO extends UserDTO {
    private Clazz clazz;
    private List<Parent> parents;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties({"teacher"})
    public static class Clazz extends ClazzDTO {
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties({"username", "password", "email", "avatar", "districtId", "districtName", "schoolId", "schoolName"})
    public static class Parent extends UserDTO {
        private String job;
    }

}
