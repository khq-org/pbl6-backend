package com.backend.pbl6schoolsystem.service;

import com.backend.pbl6schoolsystem.model.dto.common.SchoolDTO;
import com.backend.pbl6schoolsystem.model.entity.SchoolEntity;
import com.backend.pbl6schoolsystem.request.school.CreateSchoolRequest;
import com.backend.pbl6schoolsystem.request.school.ListSchoolRequest;
import com.backend.pbl6schoolsystem.request.school.UpdateSchoolRequest;
import com.backend.pbl6schoolsystem.response.NoContentResponse;
import com.backend.pbl6schoolsystem.response.OnlyIdResponse;
import com.backend.pbl6schoolsystem.response.school.GetSchoolResponse;
import com.backend.pbl6schoolsystem.response.school.ListSchoolResponse;
import org.springframework.stereotype.Service;

@Service
public interface SchoolService {
    OnlyIdResponse createSchool(CreateSchoolRequest request);

    OnlyIdResponse updateSchool(Long schoolId, UpdateSchoolRequest request);

    NoContentResponse deleteSchool(Long schoolId);

    GetSchoolResponse getSchool(Long schoolId);

    ListSchoolResponse getListSchool(ListSchoolRequest request);
}
