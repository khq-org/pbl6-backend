package com.backend.pbl6schoolsystem.model.dto.student;

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
public class StudentDTO implements Serializable {
    @JsonIgnoreProperties({"password", "workingPosition", "recruitmentDay"})
    private UserDTO student;
    private List<Clazz> classes;
    private List<UserDTO> parents;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(setterPrefix = "set")
    public static class Clazz implements Serializable {
        private Long clazzId;
        private String clazz;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties({"username", "password", "workingPosition", "studentId", "role", "recruitmentDay", "email"})
    public static class Parent extends UserDTO {
    }
}
