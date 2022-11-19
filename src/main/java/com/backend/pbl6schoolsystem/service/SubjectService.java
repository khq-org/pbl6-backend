package com.backend.pbl6schoolsystem.service;

import com.backend.pbl6schoolsystem.request.subject.GetListSubjectRequest;
import com.backend.pbl6schoolsystem.response.subject.GetListSubjectResponse;

public interface SubjectService {
    GetListSubjectResponse getListSubject(GetListSubjectRequest request);
}
