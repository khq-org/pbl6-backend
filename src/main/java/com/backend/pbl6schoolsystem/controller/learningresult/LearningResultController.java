package com.backend.pbl6schoolsystem.controller.learningresult;

import com.backend.pbl6schoolsystem.converter.LearningResultConverter;
import com.backend.pbl6schoolsystem.model.dto.common.ListDTO;
import com.backend.pbl6schoolsystem.model.dto.common.OnlyIdDTO;
import com.backend.pbl6schoolsystem.model.dto.student.ExamResultDTO;
import com.backend.pbl6schoolsystem.model.dto.student.LearningResultDetailDTO;
import com.backend.pbl6schoolsystem.request.leaningresult.LoadExamResultRequest;
import com.backend.pbl6schoolsystem.response.Response;
import com.backend.pbl6schoolsystem.response.learningresult.LearningResultDetailResponse;
import com.backend.pbl6schoolsystem.response.learningresult.LoadExamResultResponse;
import com.backend.pbl6schoolsystem.service.LearningResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Learning Result", description = "LearningResult APIs")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LearningResultController {
    private final LearningResultService learningResultService;
    private final LearningResultConverter learningResultConverter;

    @Operation(summary = "Get LearningResultDetail")
    @GetMapping("/learningresults/{id}")
    public Response<LearningResultDetailDTO> getLearningResultDetail(@PathVariable("id") Long learningResultId) {
        LearningResultDetailResponse response = learningResultService.getLearningResultDetail(learningResultId);
        return learningResultConverter.getResponse(response);
    }

    @Operation(summary = "Load ExamResults")
    @GetMapping("/examresults")
    public Response<List<ExamResultDTO>> loadExamResultStudent(@ModelAttribute @Valid LoadExamResultRequest request) {
        LoadExamResultResponse response = learningResultService.loadExamResult(request);
        return learningResultConverter.getResponse(response);
    }
}
