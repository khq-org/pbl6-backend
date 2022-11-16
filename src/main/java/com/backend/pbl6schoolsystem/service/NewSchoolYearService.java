package com.backend.pbl6schoolsystem.service;

import com.backend.pbl6schoolsystem.request.schoolyear.CreateUpdateSchoolYearRequest;
import com.backend.pbl6schoolsystem.request.schoolyear.NewSchoolYearRequest;
import com.backend.pbl6schoolsystem.response.NoContentResponse;
import com.backend.pbl6schoolsystem.response.OnlyIdResponse;
import com.backend.pbl6schoolsystem.response.schoolyear.ListSchoolYearResponse;

public interface NewSchoolYearService {
    ListSchoolYearResponse getListSchoolYear();
    OnlyIdResponse createSchoolYear(CreateUpdateSchoolYearRequest request);

    OnlyIdResponse updateSchoolYear(Long schoolYearId, CreateUpdateSchoolYearRequest request);

    NoContentResponse startNewSchoolYear(NewSchoolYearRequest request);
}
