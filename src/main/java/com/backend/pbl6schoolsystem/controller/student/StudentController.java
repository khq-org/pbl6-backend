package com.backend.pbl6schoolsystem.controller.student;

import com.backend.pbl6schoolsystem.converter.UserConverter;
import com.backend.pbl6schoolsystem.model.dto.common.ListDTO;
import com.backend.pbl6schoolsystem.model.dto.common.MessageDTO;
import com.backend.pbl6schoolsystem.model.dto.common.OnlyIdDTO;
import com.backend.pbl6schoolsystem.model.dto.common.UserDTO;
import com.backend.pbl6schoolsystem.model.dto.student.ProfileStudentDTO;
import com.backend.pbl6schoolsystem.model.dto.student.StudentDTO;
import com.backend.pbl6schoolsystem.request.leaningresult.CreateUpdateLearningResultRequest;
import com.backend.pbl6schoolsystem.request.student.CreateStudentRequest;
import com.backend.pbl6schoolsystem.request.student.GetProfileStudentRequest;
import com.backend.pbl6schoolsystem.request.student.ListStudentRequest;
import com.backend.pbl6schoolsystem.response.NoContentResponse;
import com.backend.pbl6schoolsystem.response.OnlyIdResponse;
import com.backend.pbl6schoolsystem.response.Response;
import com.backend.pbl6schoolsystem.response.student.GetProfileStudentResponse;
import com.backend.pbl6schoolsystem.response.student.GetStudentResponse;
import com.backend.pbl6schoolsystem.response.user.ListUserResponse;
import com.backend.pbl6schoolsystem.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Student", description = "Student APIs")
@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;
    private final UserConverter userConverter;

    @Operation(summary = "List student")
    @GetMapping
    public Response<ListDTO<UserDTO>> getListStudent(@ModelAttribute ListStudentRequest request) {
        ListUserResponse response = studentService.getListStudent(request);
        return userConverter.getResponse(response);
    }

    @Operation(summary = "Get student")
    @GetMapping("/{id}")
    public Response<StudentDTO> getStudent(@PathVariable("id") Long studentId) {
        GetStudentResponse response = studentService.getStudent(studentId);
        return userConverter.getResponse(response);
    }

    @Operation(summary = "Profile student")
    @GetMapping("/profile")
    public Response<ProfileStudentDTO> getProfileStudent(@ModelAttribute @Valid GetProfileStudentRequest request) {
        GetProfileStudentResponse response = studentService.getProfileStudent(request);
        if (response.getSuccess()) {
            return userConverter.getResponse(response);
        }
        return userConverter.getError(response.getErrorResponse());
    }

    @Operation(summary = "Update learning result in profile student")
    @PutMapping("profile/learningResult/{id}")
    public Response<OnlyIdDTO> updateLearningResultForProfileStudent(@PathVariable("id") Long learningResultId, @RequestBody CreateUpdateLearningResultRequest request) {
        OnlyIdResponse response = studentService.updateLearningResultForProfileStudent(learningResultId, request);
        if (response.getSuccess()) {
            return userConverter.getResponse(response);
        }
        return userConverter.getError(response.getErrorResponse());
    }

    @Operation(summary = "Create student")
    @PostMapping
    public Response<OnlyIdDTO> createStudent(@RequestBody CreateStudentRequest request) {
        OnlyIdResponse response = studentService.createStudent(request);
        if (response.getSuccess()) {
            return userConverter.getResponse(response);
        } else {
            return userConverter.getError(response.getErrorResponse());
        }
    }

    @Operation(summary = "Update student")
    @PutMapping("/{id}")
    public Response<OnlyIdDTO> updateStudent(@PathVariable("id") Long studentId, @RequestBody StudentDTO request) {
        OnlyIdResponse response = studentService.updateStudent(studentId, request);
        if (response.getSuccess()) {
            return userConverter.getResponse(response);
        } else {
            return userConverter.getError(response.getErrorResponse());
        }
    }

    @Operation(summary = "Delete student")
    @DeleteMapping("/{id}")
    public Response<MessageDTO> deleteStudent(@PathVariable("id") Long studentId) {
        NoContentResponse response = studentService.deleteStudent(studentId);
        if (response.getSuccess()) {
            return userConverter.getResponse(response);
        } else {
            return userConverter.getError(response.getErrorResponse());
        }
    }
}
