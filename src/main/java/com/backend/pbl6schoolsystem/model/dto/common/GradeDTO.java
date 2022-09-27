package com.backend.pbl6schoolsystem.model.dto.common;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeDTO implements Serializable {
    private Long gradeId;
    private String grade;
}
