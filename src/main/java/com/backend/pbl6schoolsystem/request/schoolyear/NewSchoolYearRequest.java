package com.backend.pbl6schoolsystem.request.schoolyear;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NewSchoolYearRequest {
    private Long newSchoolYearId;
    private List<Long> oldClassIds;
    private List<Long> newClassIds;
    private List<Long> teacherIds;
}
