package com.backend.pbl6schoolsystem.response;

import com.backend.pbl6schoolsystem.model.dto.common.ErrorDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Builder(setterPrefix = "set")
public class Response<T> implements Serializable {
    private Boolean success;
    private String message;
    private List<ErrorDTO> errorDTOs;
    private T data;
    private Timestamp timestamp;
}
