package com.backend.pbl6schoolsystem.model.dto.common;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectDTO implements Serializable {
    private Long subjectId;
    private String subject;
    private String code;
    private String description;
}
