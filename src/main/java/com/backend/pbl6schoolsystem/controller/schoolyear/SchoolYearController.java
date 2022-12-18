package com.backend.pbl6schoolsystem.controller.schoolyear;

import com.backend.pbl6schoolsystem.converter.SchoolYearConverter;
import com.backend.pbl6schoolsystem.model.dto.common.ListDTO;
import com.backend.pbl6schoolsystem.model.dto.common.MessageDTO;
import com.backend.pbl6schoolsystem.model.dto.common.OnlyIdDTO;
import com.backend.pbl6schoolsystem.model.dto.common.SchoolYearDTO;
import com.backend.pbl6schoolsystem.request.schoolyear.CreateUpdateSchoolYearRequest;
import com.backend.pbl6schoolsystem.request.schoolyear.NewSchoolYearRequest;
import com.backend.pbl6schoolsystem.response.NoContentResponse;
import com.backend.pbl6schoolsystem.response.OnlyIdResponse;
import com.backend.pbl6schoolsystem.response.Response;
import com.backend.pbl6schoolsystem.response.schoolyear.ListSchoolYearResponse;
import com.backend.pbl6schoolsystem.service.NewSchoolYearService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "SchoolYear", description = "SchoolYear APIs")
@RestController
@RequestMapping("/api/schoolyear")
@RequiredArgsConstructor

public class SchoolYearController {
    private final NewSchoolYearService schoolYearService;
    private final SchoolYearConverter schoolYearConverter;

    @Operation(summary = "List school year")
    @GetMapping
    public Response<ListDTO<SchoolYearDTO>> getListSchoolYear() {
        ListSchoolYearResponse response = schoolYearService.getListSchoolYear();
        return schoolYearConverter.getResponse(response);
    }

    @Operation(summary = "Create school year")
    @PostMapping
    public Response<OnlyIdDTO> createSchoolYear(@RequestBody CreateUpdateSchoolYearRequest request) {
        OnlyIdResponse response = schoolYearService.createSchoolYear(request);
        if (response.getSuccess()) {
            return schoolYearConverter.getResponse(response);
        }
        return schoolYearConverter.getError(response.getErrorResponse());
    }

    @Operation(summary = "Update school year")
    @PutMapping("/{id}")
    public Response<OnlyIdDTO> updateSchoolYear(@PathVariable("id") Long schoolYearId, @RequestBody CreateUpdateSchoolYearRequest request) {
        OnlyIdResponse response = schoolYearService.updateSchoolYear(schoolYearId, request);
        if (response.getSuccess()) {
            return schoolYearConverter.getResponse(response);
        }
        return schoolYearConverter.getError(response.getErrorResponse());
    }

    @Operation(summary = "Start new school year")
    @PostMapping("/startNewSchoolYear")
    public Response<MessageDTO> startNewSchoolYear(@RequestBody NewSchoolYearRequest request) {
        NoContentResponse response = schoolYearService.startNewSchoolYear(request);
        if (response.getSuccess()) {
            return schoolYearConverter.getResponse(response);
        }
        return schoolYearConverter.getError(response.getErrorResponse());
    }
}
