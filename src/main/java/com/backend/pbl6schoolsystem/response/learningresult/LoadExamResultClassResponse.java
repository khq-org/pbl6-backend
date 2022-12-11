package com.backend.pbl6schoolsystem.response.learningresult;

import com.backend.pbl6schoolsystem.model.dto.clazz.ExamResultClassDTO;
import com.backend.pbl6schoolsystem.response.ErrorResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder(setterPrefix = "set")
public class LoadExamResultClassResponse implements Serializable {
    private Boolean success;
    private ErrorResponse errorResponse;
    private ExamResultClassDTO examResultClass;
}
