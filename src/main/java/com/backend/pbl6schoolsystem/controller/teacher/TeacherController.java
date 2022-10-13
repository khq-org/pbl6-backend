package com.backend.pbl6schoolsystem.controller.teacher;

import com.backend.pbl6schoolsystem.converter.UserConverter;
import com.backend.pbl6schoolsystem.model.dto.common.ListDTO;
import com.backend.pbl6schoolsystem.model.dto.common.OnlyIdDTO;
import com.backend.pbl6schoolsystem.model.dto.common.UserDTO;
import com.backend.pbl6schoolsystem.request.teacher.CreateTeacherRequest;
import com.backend.pbl6schoolsystem.request.teacher.ListTeacherRequest;
import com.backend.pbl6schoolsystem.response.OnlyIdResponse;
import com.backend.pbl6schoolsystem.response.Response;
import com.backend.pbl6schoolsystem.response.user.ListUserResponse;
import com.backend.pbl6schoolsystem.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeacherController {
    private final UserConverter userConverter;
    private final TeacherService teacherService;

    @Operation(summary = "List teacher")
    @GetMapping
    public Response<ListDTO<UserDTO>> getListTeacher(@ModelAttribute @Valid ListTeacherRequest request) {
        ListUserResponse response = teacherService.getListTeacher(request);
        return userConverter.getResponse(response);
    }

    @Operation(summary = "Create teacher")
    @PostMapping
    public Response<OnlyIdDTO> createTeacher(@RequestBody CreateTeacherRequest request) {
        OnlyIdResponse response = teacherService.createTeacher(request);
        if (response.getSuccess()) {
            return userConverter.getResponse(response);
        }
        return userConverter.getError(response.getErrorResponse());
    }
}
