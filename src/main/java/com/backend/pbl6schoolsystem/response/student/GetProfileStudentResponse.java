package com.backend.pbl6schoolsystem.response.student;

import com.backend.pbl6schoolsystem.model.dto.student.ProfileStudentDTO;
import com.backend.pbl6schoolsystem.response.ErrorResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "set")
public class GetProfileStudentResponse {
    private Boolean success;
    private ErrorResponse errorResponse;
    private ProfileStudentDTO profileStudentDTO;
}
