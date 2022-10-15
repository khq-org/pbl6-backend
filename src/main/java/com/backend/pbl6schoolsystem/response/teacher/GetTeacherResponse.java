package com.backend.pbl6schoolsystem.response.teacher;

import com.backend.pbl6schoolsystem.model.dto.teacher.TeacherDTO;
import com.backend.pbl6schoolsystem.response.ErrorResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "set")
public class GetTeacherResponse {
    private Boolean success;
    private ErrorResponse errorResponse;
    private TeacherDTO teacher;
}
