package com.backend.pbl6schoolsystem.response.school;

import com.backend.pbl6schoolsystem.model.dto.common.SchoolDTO;
import com.backend.pbl6schoolsystem.response.ErrorResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "set")
public class GetSchoolResponse {
    private Boolean success;
    private ErrorResponse errorResponse;
    private SchoolDTO schoolDTO;
}
