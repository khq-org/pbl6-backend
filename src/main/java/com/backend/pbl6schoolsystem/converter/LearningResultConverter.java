package com.backend.pbl6schoolsystem.converter;

import com.backend.pbl6schoolsystem.model.dto.student.LearningResultDetailDTO;
import com.backend.pbl6schoolsystem.response.Response;
import com.backend.pbl6schoolsystem.response.learningresult.LearningResultDetailResponse;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class LearningResultConverter extends CommonConverter {
    public Response<LearningResultDetailDTO> getResponse(LearningResultDetailResponse response) {
        return Response.<LearningResultDetailDTO>builder()
                .setSuccess(true)
                .setData(response.getLearningResultDetail())
                .setTimestamp(new Timestamp(System.currentTimeMillis()))
                .build();
    }
}
