package com.backend.pbl6schoolsystem.service;

import com.backend.pbl6schoolsystem.request.student.CreateStudentRequest;
import com.backend.pbl6schoolsystem.request.student.ListStudentRequest;
import com.backend.pbl6schoolsystem.response.OnlyIdResponse;
import com.backend.pbl6schoolsystem.response.student.ListStudentResponse;

public interface StudentService {
    OnlyIdResponse createStudent(CreateStudentRequest request);
    ListStudentResponse getListStudent(ListStudentRequest request);
}
