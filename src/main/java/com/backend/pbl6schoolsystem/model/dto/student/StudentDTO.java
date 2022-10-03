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
public class StudentDTO implements Serializable {
    private UserDTO userDTO;
    private Integer studentId;
    private Clazz clazz;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(setterPrefix = "set")
    public static class Clazz implements Serializable {
        private Long clazzId;
        private String clazz;
    }

}
