package com.backend.pbl6schoolsystem.service;

import com.backend.pbl6schoolsystem.request.student.CreateProfileRequest;
import com.backend.pbl6schoolsystem.request.student.UpdateProfileRequest;
import com.backend.pbl6schoolsystem.response.OnlyIdResponse;

public interface ProfileService {
    OnlyIdResponse createProfileStudent(CreateProfileRequest request);
    OnlyIdResponse updateProfileStudent(UpdateProfileRequest request);
}
