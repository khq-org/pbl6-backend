package com.backend.pbl6schoolsystem.common.exception;

import com.backend.pbl6schoolsystem.model.dto.common.ErrorDTO;
import com.backend.pbl6schoolsystem.response.ErrorResponse;
import lombok.Getter;

import java.util.List;

@Getter
public class ErrorResponseRuntimeException extends RuntimeException {
    private ErrorResponse errorResponse;
    private List<ErrorDTO> errorDTOs;

    public ErrorResponseRuntimeException(ErrorResponse errorResponse, List<ErrorDTO> errorDTOs) {
        this.errorResponse = errorResponse;
        this.errorDTOs = errorDTOs;
    }
}
