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
    private Boolean specializedClass;
    private String subject;
    private Long teacherId;
    private String teacher;
}
