package com.backend.pbl6schoolsystem.response.learningresult;

import com.backend.pbl6schoolsystem.model.dto.student.ExamResultDTO;
import com.backend.pbl6schoolsystem.response.ErrorResponse;
import com.backend.pbl6schoolsystem.response.PageResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "set")
public class LoadExamResultResponse {
    private Boolean success;
    private ErrorResponse errorResponse;
    private PageResponse pageResponse;
    private List<ExamResultDTO> examResult;
}
