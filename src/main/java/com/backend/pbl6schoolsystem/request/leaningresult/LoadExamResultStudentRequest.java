package com.backend.pbl6schoolsystem.request.leaningresult;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "set")
public class LoadExamResultStudentRequest {
    private Long studentId;
    private Long semesterId;
    private Long schoolYearId;
    private Long subjectId;
}
