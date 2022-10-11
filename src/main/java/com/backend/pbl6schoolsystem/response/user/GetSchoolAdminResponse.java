package com.backend.pbl6schoolsystem.response.user;

import com.backend.pbl6schoolsystem.model.dto.user.SchoolAdminDTO;
import com.backend.pbl6schoolsystem.response.ErrorResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "set")
public class GetSchoolAdminResponse {
    private Boolean success;
    private ErrorResponse errorResponse;
    private SchoolAdminDTO schoolAdminDTO;
}
