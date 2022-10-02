package com.backend.pbl6schoolsystem.controller;

import com.backend.pbl6schoolsystem.converter.SchoolConverter;
import com.backend.pbl6schoolsystem.model.dto.common.ListDTO;
import com.backend.pbl6schoolsystem.model.dto.common.MessageDTO;
import com.backend.pbl6schoolsystem.model.dto.common.OnlyIdDTO;
import com.backend.pbl6schoolsystem.model.dto.common.SchoolDTO;
import com.backend.pbl6schoolsystem.model.entity.SchoolEntity;
import com.backend.pbl6schoolsystem.request.school.CreateSchoolRequest;
import com.backend.pbl6schoolsystem.request.school.ListSchoolRequest;
import com.backend.pbl6schoolsystem.request.school.UpdateSchoolRequest;
import com.backend.pbl6schoolsystem.response.NoContentResponse;
import com.backend.pbl6schoolsystem.response.OnlyIdResponse;
import com.backend.pbl6schoolsystem.response.Response;
import com.backend.pbl6schoolsystem.response.school.ListSchoolResponse;
import com.backend.pbl6schoolsystem.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schools")
@RequiredArgsConstructor
public class SchoolController {
    private final SchoolService schoolService;
    private final SchoolConverter schoolConverter;

    @GetMapping
    public Response<ListDTO<SchoolDTO>> getListSchool(@ModelAttribute ListSchoolRequest request) {
        ListSchoolResponse response = schoolService.getListSchool(request);
        return schoolConverter.getResponse(response);
    }

    @GetMapping("/{id}")
    public Response<SchoolDTO> getSchool(@PathVariable("id") Long schoolId) {
        SchoolEntity response = schoolService.getSchool(schoolId);
        return schoolConverter.getResponse(response);
    }

    @PostMapping
    public Response<OnlyIdDTO> createSchool(@RequestBody CreateSchoolRequest request) {
        OnlyIdResponse response = schoolService.createSchool(request);
        if (response.getSuccess()) {
            return schoolConverter.getResponse(response);
        } else {
            return schoolConverter.getError(response.getErrorResponse());
        }
    }

    @PutMapping("/{id}")
    public Response<OnlyIdDTO> updateSchool(@PathVariable("id") Long schoolId, @RequestBody UpdateSchoolRequest request) {
        OnlyIdResponse response = schoolService.updateSchool(schoolId, request);
        if (response.getSuccess()) {
            return schoolConverter.getResponse(response);
        } else {
            return schoolConverter.getError(response.getErrorResponse());
        }
    }

    @DeleteMapping("/{id}")
    public Response<MessageDTO> deleteSchool(@PathVariable("id") Long schoolId) {
        NoContentResponse response = schoolService.deleteSchool(schoolId);
        if (response.getSuccess()) {
            return schoolConverter.getResponse(response);
        } else {
            return schoolConverter.getError(response.getErrorResponse());
        }
    }
}
