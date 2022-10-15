package com.backend.pbl6schoolsystem.response;

import com.backend.pbl6schoolsystem.model.dto.common.UserDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder(setterPrefix = "set")
public class UserInfoResponse implements Serializable {
    private Boolean success;
    private ErrorResponse errorResponse;
    private UserDTO user;
    private List<String> authorities;
}
