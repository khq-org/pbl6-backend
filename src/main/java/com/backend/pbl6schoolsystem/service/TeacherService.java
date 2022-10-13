package com.backend.pbl6schoolsystem.service;

import com.backend.pbl6schoolsystem.request.teacher.CreateTeacherRequest;
import com.backend.pbl6schoolsystem.request.teacher.ListTeacherRequest;
import com.backend.pbl6schoolsystem.response.OnlyIdResponse;
import com.backend.pbl6schoolsystem.response.user.ListUserResponse;

public interface TeacherService {
    ListUserResponse getListTeacher(ListTeacherRequest request);

    OnlyIdResponse createTeacher(CreateTeacherRequest request);
}
