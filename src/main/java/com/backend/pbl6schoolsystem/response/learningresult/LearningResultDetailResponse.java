package com.backend.pbl6schoolsystem.response.learningresult;

import com.backend.pbl6schoolsystem.model.dto.student.LearningResultDetailDTO;
import com.backend.pbl6schoolsystem.response.ErrorResponse;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "set")
public class LearningResultDetailResponse implements Serializable {
    private Boolean success;
    private ErrorResponse errorResponse;
    private LearningResultDetailDTO learningResultDetail;
}
