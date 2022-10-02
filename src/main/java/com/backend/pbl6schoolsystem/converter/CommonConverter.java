package com.backend.pbl6schoolsystem.converter;

import com.backend.pbl6schoolsystem.common.exception.ErrorResponseRuntimeException;
import com.backend.pbl6schoolsystem.model.dto.common.ErrorDTO;
import com.backend.pbl6schoolsystem.response.ErrorResponse;
import com.backend.pbl6schoolsystem.response.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommonConverter {
    public <T> Response<T> getError(ErrorResponse errorResponse) {
        throw new ErrorResponseRuntimeException(errorResponse, getErrorDTOs(errorResponse.getErrors()));
    }

    public List<ErrorDTO> getErrorDTOs(Map<String, String> errors) {
        List<ErrorDTO> errorDTOS = new ArrayList<>();
        for (var v : errors.entrySet()) {
            errorDTOS.add(ErrorDTO.builder()
                    .setKey(v.getKey())
                    .setValue(v.getValue())
                    .build());
        }
        return errorDTOS;
    }
}
