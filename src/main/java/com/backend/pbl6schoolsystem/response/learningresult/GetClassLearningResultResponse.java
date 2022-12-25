package com.backend.pbl6schoolsystem.response.learningresult;

import com.backend.pbl6schoolsystem.model.dto.clazz.ClassLearningResultDTO;
import com.backend.pbl6schoolsystem.response.ErrorResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "set")
public class GetClassLearningResultResponse {
    private Boolean success;
    private ErrorResponse errorResponse;
    private ClassLearningResultDTO classLearningResult;
}
