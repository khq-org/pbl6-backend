package com.backend.pbl6schoolsystem.service;

import com.backend.pbl6schoolsystem.request.user.ChangePasswordRequest;
import com.backend.pbl6schoolsystem.request.user.UpdateUserRequest;
import com.backend.pbl6schoolsystem.response.NoContentResponse;
import com.backend.pbl6schoolsystem.response.OnlyIdResponse;
import com.backend.pbl6schoolsystem.response.UserInfoResponse;
import com.backend.pbl6schoolsystem.response.calendar.ListCalendarResponse;

public interface UserService {
    // --------------- My account ---------------
    UserInfoResponse getInfoAccount(Long userId);

    OnlyIdResponse updateInfoAccount(Long userId, UpdateUserRequest request);

    NoContentResponse changePassword(Long userId, ChangePasswordRequest request);

    ListCalendarResponse getListCalendar();
}
