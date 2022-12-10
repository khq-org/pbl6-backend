package com.backend.pbl6schoolsystem.request.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ChangePasswordRequest implements Serializable {
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}
