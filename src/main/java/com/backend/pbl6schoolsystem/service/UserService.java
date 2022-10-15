package com.backend.pbl6schoolsystem.service;

import com.backend.pbl6schoolsystem.response.UserInfoResponse;

public interface UserService {
    // --------------- My account ---------------
    UserInfoResponse getInfoAccount(Long userId);
}
