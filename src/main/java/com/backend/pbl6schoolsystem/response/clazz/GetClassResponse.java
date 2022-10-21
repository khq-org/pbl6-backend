package com.backend.pbl6schoolsystem.response.clazz;

import com.backend.pbl6schoolsystem.model.dto.common.ClazzDTO;
import com.backend.pbl6schoolsystem.response.ErrorResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "set")
public class GetClassResponse {
    private Boolean success;
    private ErrorResponse errorResponse;
    private ClazzDTO clazzDTO;
}
