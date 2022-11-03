package com.backend.pbl6schoolsystem.controller.student;

import com.backend.pbl6schoolsystem.request.student.CreateProfileRequest;
import com.backend.pbl6schoolsystem.response.OnlyIdResponse;
import com.backend.pbl6schoolsystem.response.Response;
import com.backend.pbl6schoolsystem.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/profiles")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping
    public Response<OnlyIdResponse> createProfile(@RequestBody CreateProfileRequest request) {
        return null;
    }
}
