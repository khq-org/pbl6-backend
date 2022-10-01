package com.backend.pbl6schoolsystem.service;

import com.backend.pbl6schoolsystem.request.school.CreateSchoolRequest;
import com.backend.pbl6schoolsystem.request.school.ListSchoolRequest;
import com.backend.pbl6schoolsystem.response.OnlyIdResponse;
import com.backend.pbl6schoolsystem.response.school.ListSchoolResponse;
import org.springframework.stereotype.Service;

@Service
public interface SchoolService {
    OnlyIdResponse createSchool(CreateSchoolRequest school);
    ListSchoolResponse getListSchool(ListSchoolRequest request);
}
