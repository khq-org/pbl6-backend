package com.backend.pbl6schoolsystem.converter;

import com.backend.pbl6schoolsystem.common.exception.ErrorResponseRuntimeException;
import com.backend.pbl6schoolsystem.model.dto.common.ErrorDTO;
import com.backend.pbl6schoolsystem.model.dto.common.MessageDTO;
import com.backend.pbl6schoolsystem.model.dto.common.OnlyIdDTO;
import com.backend.pbl6schoolsystem.model.dto.common.UserDTO;
import com.backend.pbl6schoolsystem.response.ErrorResponse;
import com.backend.pbl6schoolsystem.response.NoContentResponse;
import com.backend.pbl6schoolsystem.response.OnlyIdResponse;
import com.backend.pbl6schoolsystem.response.Response;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommonConverter {

    public Response<OnlyIdDTO> getResponse(OnlyIdResponse response) {
        return Response.<OnlyIdDTO>builder()
                .setSuccess(true)
                .setData(OnlyIdDTO.builder()
                        .setId(response.getId())
                        .setName(response.getName())
                        .build())
                .setTimestamp(new Timestamp(System.currentTimeMillis()))
                .build();
    }

    public Response<MessageDTO> getResponse(NoContentResponse response) {
        return Response.<MessageDTO>builder()
                .setSuccess(true)
                .setMessage("Deleted")
                .setTimestamp(new Timestamp(System.currentTimeMillis()))
                .build();
    }

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
