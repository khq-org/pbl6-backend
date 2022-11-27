package com.backend.pbl6schoolsystem.service;

import com.backend.pbl6schoolsystem.request.leaningresult.LoadExamResultRequest;
import com.backend.pbl6schoolsystem.response.learningresult.LearningResultDetailResponse;
import com.backend.pbl6schoolsystem.response.learningresult.LoadExamResultResponse;

public interface LearningResultService {
    LearningResultDetailResponse getLearningResultDetail(Long learningResultId);
    LoadExamResultResponse loadExamResult(LoadExamResultRequest request);
}
