package com.backend.pbl6schoolsystem.model.dto.student;

import com.backend.pbl6schoolsystem.model.dto.common.ClazzDTO;
import com.backend.pbl6schoolsystem.model.dto.common.UserDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "set")
public class StudentDetailDTO implements Serializable {
    @JsonIgnoreProperties({"password", "job", "workingPosition", "positionGroup", "recruitmentDay", "factorSalary", "rank", "level"})
    private UserDTO userDTO;
    private ClazzDTO clazz;
    private List<Parent> parents;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(setterPrefix = "set")
    public static class Parent {
        @JsonIgnoreProperties({"username", "password", "email", "avatar", "schoolId", "schoolName",
                "workingPosition", "positionGroup", "recruitmentDay", "factorSalary", "rank", "level"})
        private UserDTO userDTO;
    }

}
