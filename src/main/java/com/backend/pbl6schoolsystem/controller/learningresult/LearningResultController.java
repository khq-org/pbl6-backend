package com.backend.pbl6schoolsystem.controller.learningresult;

import com.backend.pbl6schoolsystem.converter.LearningResultConverter;
import com.backend.pbl6schoolsystem.model.dto.clazz.ExamResultClassDTO;
import com.backend.pbl6schoolsystem.model.dto.common.MessageDTO;
import com.backend.pbl6schoolsystem.model.dto.student.ExamResultDTO;
import com.backend.pbl6schoolsystem.model.dto.student.LearningResultDetailDTO;
import com.backend.pbl6schoolsystem.request.leaningresult.InputScoreRequest;
import com.backend.pbl6schoolsystem.request.leaningresult.LoadExamResultClassRequest;
import com.backend.pbl6schoolsystem.request.leaningresult.LoadExamResultStudentRequest;
import com.backend.pbl6schoolsystem.response.NoContentResponse;
import com.backend.pbl6schoolsystem.response.Response;
import com.backend.pbl6schoolsystem.response.learningresult.LearningResultDetailResponse;
import com.backend.pbl6schoolsystem.response.learningresult.LoadExamResultClassResponse;
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

    @Operation(summary = "Load ExamResults Student")
    @GetMapping("/examresults/student")
    public Response<List<ExamResultDTO>> loadExamResultStudent(@ModelAttribute @Valid LoadExamResultStudentRequest request) {
        LoadExamResultResponse response = learningResultService.loadExamResult(request);
        if (response.getSuccess()) {
            return learningResultConverter.getResponse(response);
        }
        return learningResultConverter.getError(response.getErrorResponse());
    }

    @Operation(summary = "Load ExamResult Class")
    @GetMapping("/examresults/class")
    public Response<ExamResultClassDTO> loadExamResultClass(@ModelAttribute @Valid LoadExamResultClassRequest request) {
        LoadExamResultClassResponse response = learningResultService.loadExamResultClass(request);
        if (response.getSuccess()) {
            return learningResultConverter.getResponse(response);
        }
        return learningResultConverter.getError(response.getErrorResponse());
    }

    @Operation(summary = "Input score")
    @PostMapping("/inputscores")
    public Response<MessageDTO> inputScore(@RequestBody InputScoreRequest request) {
        NoContentResponse response = learningResultService.inputScore(request);
        if (response.getSuccess()) {
            return learningResultConverter.getResponse(response);
        }
        return learningResultConverter.getError(response.getErrorResponse());
    }

//    @Operation(summary = "Modify score")
//    @PutMapping("/modifyscores")
//    public Response<MessageDTO> modifyScore(@RequestBody ModifyScoreRequest request) {
//        NoContentResponse response = learningResultService.modifyScore(request);
//        if (response.getSuccess()) {
//            return learningResultConverter.getResponse(response);
//        }
//        return learningResultConverter.getError(response.getErrorResponse());
//    }
}
