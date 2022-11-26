package com.backend.pbl6schoolsystem.service;

import com.backend.pbl6schoolsystem.response.learningresult.LearningResultDetailResponse;

public interface LearningResultService {
    LearningResultDetailResponse getLearningResultDetail(Long learningResultId);
}
