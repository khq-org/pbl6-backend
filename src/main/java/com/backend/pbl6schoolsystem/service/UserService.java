package com.backend.pbl6schoolsystem.service;

import com.backend.pbl6schoolsystem.request.user.ChangePasswordRequest;
import com.backend.pbl6schoolsystem.response.NoContentResponse;
import com.backend.pbl6schoolsystem.response.UserInfoResponse;

public interface UserService {
    // --------------- My account ---------------
    UserInfoResponse getInfoAccount(Long userId);

    NoContentResponse changePassword(Long userId, ChangePasswordRequest request);
}
