package com.backend.pbl6schoolsystem.service;

import com.backend.pbl6schoolsystem.model.dto.teacher.TeacherDTO;
import com.backend.pbl6schoolsystem.request.teacher.CreateTeacherRequest;
import com.backend.pbl6schoolsystem.request.teacher.ListTeacherRequest;
import com.backend.pbl6schoolsystem.request.user.UpdateUserRequest;
import com.backend.pbl6schoolsystem.response.NoContentResponse;
import com.backend.pbl6schoolsystem.response.OnlyIdResponse;
import com.backend.pbl6schoolsystem.response.teacher.GetTeacherResponse;
import com.backend.pbl6schoolsystem.response.user.ListUserResponse;

public interface TeacherService {
    ListUserResponse getListTeacher(ListTeacherRequest request);

    GetTeacherResponse getTeacher(Long teacherId);

    OnlyIdResponse createTeacher(CreateTeacherRequest request);

    OnlyIdResponse updateTeacher(Long teacherId, UpdateUserRequest request);

    NoContentResponse deleteTeacher(Long teacherId);


    // ANH YÊU EM NHIỀU //
}
