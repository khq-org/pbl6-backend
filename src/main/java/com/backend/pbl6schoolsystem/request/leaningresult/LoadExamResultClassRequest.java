package com.backend.pbl6schoolsystem.request.leaningresult;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class LoadExamResultClassRequest implements Serializable {
    private Long classId;
    private Long semesterId;
    private Long schoolYearId;
    private Long subjectId;
}
