package com.backend.pbl6schoolsystem.service;

import com.backend.pbl6schoolsystem.request.school.CreateSchoolAdminRequest;
import com.backend.pbl6schoolsystem.response.OnlyIdResponse;

public interface UserService {
    OnlyIdResponse createSchoolAdmin(CreateSchoolAdminRequest request);
}
