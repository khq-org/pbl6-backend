package com.backend.pbl6schoolsystem.request.leaningresult;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoadExamResultRequest {
    private Long studentId;
    private Long semesterId;
    private Long schoolYearId;
    private Long subjectId;
}
