package com.backend.pbl6schoolsystem.service;

import com.backend.pbl6schoolsystem.model.dto.student.StudentDTO;
import com.backend.pbl6schoolsystem.request.student.CreateStudentRequest;
import com.backend.pbl6schoolsystem.request.student.ListStudentRequest;
import com.backend.pbl6schoolsystem.response.NoContentResponse;
import com.backend.pbl6schoolsystem.response.OnlyIdResponse;
import com.backend.pbl6schoolsystem.response.student.GetStudentResponse;
import com.backend.pbl6schoolsystem.response.user.ListUserResponse;

public interface StudentService {
    OnlyIdResponse createStudent(CreateStudentRequest request);

    ListUserResponse getListStudent(ListStudentRequest request);

    GetStudentResponse getStudent(Long studentId);

    OnlyIdResponse updateStudent(Long studentId, StudentDTO request);

    NoContentResponse deleteStudent(Long studentId);
}
