package com.backend.pbl6schoolsystem.service;

import com.backend.pbl6schoolsystem.request.leaningresult.InputScoreRequest;
import com.backend.pbl6schoolsystem.request.leaningresult.LoadExamResultClassRequest;
import com.backend.pbl6schoolsystem.request.leaningresult.LoadExamResultStudentRequest;
import com.backend.pbl6schoolsystem.request.leaningresult.ModifyScoreRequest;
import com.backend.pbl6schoolsystem.response.NoContentResponse;
import com.backend.pbl6schoolsystem.response.learningresult.LearningResultDetailResponse;
import com.backend.pbl6schoolsystem.response.learningresult.LoadExamResultClassResponse;
import com.backend.pbl6schoolsystem.response.learningresult.LoadExamResultResponse;

public interface LearningResultService {
    LearningResultDetailResponse getLearningResultDetail(Long learningResultId);
    LoadExamResultResponse loadExamResult(LoadExamResultStudentRequest request);

    NoContentResponse inputScore(InputScoreRequest request);

//    NoContentResponse modifyScore(ModifyScoreRequest request);

    LoadExamResultClassResponse loadExamResultClass(LoadExamResultClassRequest request);
}
