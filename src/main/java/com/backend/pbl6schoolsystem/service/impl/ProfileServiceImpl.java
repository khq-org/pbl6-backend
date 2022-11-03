package com.backend.pbl6schoolsystem.service.impl;

import com.backend.pbl6schoolsystem.repository.jpa.ProfileRepository;
import com.backend.pbl6schoolsystem.request.student.CreateProfileRequest;
import com.backend.pbl6schoolsystem.request.student.UpdateProfileRequest;
import com.backend.pbl6schoolsystem.response.OnlyIdResponse;
import com.backend.pbl6schoolsystem.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;

    @Override
    public OnlyIdResponse createProfileStudent(CreateProfileRequest request) {
        return null;
    }

    @Override
    public OnlyIdResponse updateProfileStudent(UpdateProfileRequest request) {
        return null;
    }
}
