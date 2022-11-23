package com.backend.pbl6schoolsystem.model.dto.common;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "set")
public class ClazzDTO implements Serializable {
    private Long classId;
    private String clazz;
    private GradeDTO grade;
    private Teacher teacher;
    private Boolean specializedClass;
    private String subject;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(setterPrefix = "set")
    public static class Teacher {
        private Long teacherId;
        private String teacher;
    }

}
