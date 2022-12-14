package com.backend.pbl6schoolsystem.converter;

import com.backend.pbl6schoolsystem.model.dto.clazz.ClassLearningResultDTO;
import com.backend.pbl6schoolsystem.model.dto.clazz.ExamResultClassDTO;
import com.backend.pbl6schoolsystem.model.dto.common.ListDTO;
import com.backend.pbl6schoolsystem.model.dto.student.ExamResultDTO;
import com.backend.pbl6schoolsystem.model.dto.student.LearningResultDetailDTO;
import com.backend.pbl6schoolsystem.response.Response;
import com.backend.pbl6schoolsystem.response.learningresult.GetClassLearningResultResponse;
import com.backend.pbl6schoolsystem.response.learningresult.LearningResultDetailResponse;
import com.backend.pbl6schoolsystem.response.learningresult.LoadExamResultClassResponse;
import com.backend.pbl6schoolsystem.response.learningresult.LoadExamResultResponse;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
public class LearningResultConverter extends CommonConverter {
    public Response<LearningResultDetailDTO> getResponse(LearningResultDetailResponse response) {
        return Response.<LearningResultDetailDTO>builder()
                .setSuccess(true)
                .setData(response.getLearningResultDetail())
                .setTimestamp(new Timestamp(System.currentTimeMillis()))
                .build();
    }

    public Response<List<ExamResultDTO>> getResponse(LoadExamResultResponse response) {
        return Response.<List<ExamResultDTO>>builder()
                .setSuccess(true)
                .setData(response.getExamResult())
                .setTimestamp(new Timestamp(System.currentTimeMillis()))
                .build();
    }

    public Response<ExamResultClassDTO> getResponse(LoadExamResultClassResponse response) {
        return Response.<ExamResultClassDTO>builder()
                .setSuccess(true)
                .setData(response.getExamResultClass())
                .setTimestamp(new Timestamp(System.currentTimeMillis()))
                .build();
    }

    public Response<ClassLearningResultDTO> getResponse(GetClassLearningResultResponse response) {
        return Response.<ClassLearningResultDTO>builder()
                .setSuccess(true)
                .setData(response.getClassLearningResult())
                .setTimestamp(new Timestamp(System.currentTimeMillis()))
                .build();
    }
}
