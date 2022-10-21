package com.backend.pbl6schoolsystem.request.clazz;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUpdateClassRequest {
    private String className;
    private Long gradeId;
    private Boolean isSpecializedClass;
    private String subject;
}
