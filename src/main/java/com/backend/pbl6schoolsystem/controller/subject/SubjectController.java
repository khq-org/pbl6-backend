package com.backend.pbl6schoolsystem.controller.subject;

import com.backend.pbl6schoolsystem.converter.SubjectConverter;
import com.backend.pbl6schoolsystem.model.dto.common.ListDTO;
import com.backend.pbl6schoolsystem.model.dto.common.SubjectDTO;
import com.backend.pbl6schoolsystem.request.subject.GetListSubjectRequest;
import com.backend.pbl6schoolsystem.response.Response;
import com.backend.pbl6schoolsystem.response.subject.GetListSubjectResponse;
import com.backend.pbl6schoolsystem.service.SubjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subjects")
@Tag(name = "Subject", description = "Subject APIs")
public class SubjectController {
    private final SubjectService subjectService;
    private final SubjectConverter subjectConverter;

    @GetMapping
    @Operation(summary = "List subject")
    public Response<ListDTO<SubjectDTO>> getListSubject(@ModelAttribute @Valid GetListSubjectRequest request) {
        GetListSubjectResponse response = subjectService.getListSubject(request);
        return subjectConverter.getResponse(response);
    }
}
