package com.backend.pbl6schoolsystem.service;

import com.backend.pbl6schoolsystem.request.schoolyear.CreateUpdateSchoolYearRequest;
import com.backend.pbl6schoolsystem.request.schoolyear.NewSchoolYearRequest;
import com.backend.pbl6schoolsystem.response.NoContentResponse;
import com.backend.pbl6schoolsystem.response.OnlyIdResponse;

public interface NewSchoolYearService {
    OnlyIdResponse createSchoolYear(CreateUpdateSchoolYearRequest request);

    OnlyIdResponse updateSchoolYear(Long schoolYearId, CreateUpdateSchoolYearRequest request);

    NoContentResponse startNewSchoolYear(NewSchoolYearRequest request);
}
