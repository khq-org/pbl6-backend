package com.backend.pbl6schoolsystem.controller.school;

import com.backend.pbl6schoolsystem.converter.UserConverter;
import com.backend.pbl6schoolsystem.model.dto.common.OnlyIdDTO;
import com.backend.pbl6schoolsystem.model.dto.common.UserDTO;
import com.backend.pbl6schoolsystem.request.school.CreateSchoolAdminRequest;
import com.backend.pbl6schoolsystem.response.OnlyIdResponse;
import com.backend.pbl6schoolsystem.response.Response;
import com.backend.pbl6schoolsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/schooladmins")
@RequiredArgsConstructor
public class SchoolAdminController {
    private final UserService userService;
    private final UserConverter userConverter;

    @PostMapping
    private Response<OnlyIdDTO> createSchoolAdmin(@RequestBody CreateSchoolAdminRequest request) {
        OnlyIdResponse response = userService.createSchoolAdmin(request);
        if (response.getSuccess()) {
            return userConverter.getResponse(response);
        } else {
            return userConverter.getError(response.getErrorResponse());
        }
    }
}
