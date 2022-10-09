package com.backend.pbl6schoolsystem.controller.teacher;

import com.backend.pbl6schoolsystem.request.teacher.CreateTeacherRequest;
import com.backend.pbl6schoolsystem.response.OnlyIdResponse;
import com.backend.pbl6schoolsystem.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeacherController {
    @PostMapping
    public Response<OnlyIdResponse> createTeacher(@RequestBody CreateTeacherRequest request) {
        return null;
    }
}
