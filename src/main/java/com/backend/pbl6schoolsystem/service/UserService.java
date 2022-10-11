package com.backend.pbl6schoolsystem.service;

import com.backend.pbl6schoolsystem.request.school.CreateSchoolAdminRequest;
import com.backend.pbl6schoolsystem.request.user.ListSchoolAdminRequest;
import com.backend.pbl6schoolsystem.response.user.GetSchoolAdminResponse;
import com.backend.pbl6schoolsystem.response.user.ListUserResponse;
import com.backend.pbl6schoolsystem.response.OnlyIdResponse;

public interface UserService {
    // --------------- School admin ---------------
    OnlyIdResponse createSchoolAdmin(CreateSchoolAdminRequest request);

    OnlyIdResponse updateSchoolAdmin(Long schoolAdminId, CreateSchoolAdminRequest request);

    ListUserResponse getListSchoolAdmin(ListSchoolAdminRequest request);

    GetSchoolAdminResponse getSchoolAdmin(Long schoolAdminId);
}
