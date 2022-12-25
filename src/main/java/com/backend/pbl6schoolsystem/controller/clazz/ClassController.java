package com.backend.pbl6schoolsystem.controller.clazz;

import com.backend.pbl6schoolsystem.converter.ClazzConverter;
import com.backend.pbl6schoolsystem.model.dto.common.ClazzDTO;
import com.backend.pbl6schoolsystem.model.dto.common.ListDTO;
import com.backend.pbl6schoolsystem.model.dto.common.MessageDTO;
import com.backend.pbl6schoolsystem.model.dto.common.OnlyIdDTO;
import com.backend.pbl6schoolsystem.request.clazz.CreateUpdateClassRequest;
import com.backend.pbl6schoolsystem.request.clazz.ListClassRequest;
import com.backend.pbl6schoolsystem.response.NoContentResponse;
import com.backend.pbl6schoolsystem.response.OnlyIdResponse;
import com.backend.pbl6schoolsystem.response.Response;
import com.backend.pbl6schoolsystem.response.clazz.GetClassResponse;
import com.backend.pbl6schoolsystem.response.clazz.ListClassResponse;
import com.backend.pbl6schoolsystem.service.ClazzService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Class", description = "Class APIs")
@RestController
@RequestMapping("/api/classes")
@RequiredArgsConstructor
public class ClassController {
    private final ClazzService clazzService;
    private final ClazzConverter clazzConverter;

    @Operation(summary = "List class")
    @GetMapping
    public Response<ListDTO<ClazzDTO>> getListClass(@ModelAttribute @Valid ListClassRequest request) {
        ListClassResponse response = clazzService.getListClass(request);
        if (response.getSuccess()) {
            return clazzConverter.getResponse(response);
        }
        return clazzConverter.getError(response.getErrorResponse());
    }

    @Operation(summary = "Get class")
    @GetMapping("/{id}")
    public Response<ClazzDTO> getClass(@PathVariable("id") Long clazzId) {
        GetClassResponse response = clazzService.getClass(clazzId);
        return clazzConverter.getResponse(response);
    }

    @Operation(summary = "Create class")
    @PostMapping
    public Response<OnlyIdDTO> createClass(@RequestBody CreateUpdateClassRequest request) {
        OnlyIdResponse response = clazzService.createClass(request);
        if (response.getSuccess()) {
            return clazzConverter.getResponse(response);
        }
        return clazzConverter.getError(response.getErrorResponse());
    }

    @Operation(summary = "Update class")
    @PutMapping("/{id}")
    public Response<OnlyIdDTO> updateClass(@PathVariable("id") Long clazzId, @RequestBody CreateUpdateClassRequest request) {
        OnlyIdResponse response = clazzService.updateClass(clazzId, request);
        if (response.getSuccess()) {
            return clazzConverter.getResponse(response);
        }
        return clazzConverter.getError(response.getErrorResponse());
    }

    @Operation(summary = "Delete class")
    @DeleteMapping("/{id}")
    public Response<MessageDTO> deleteClass(@PathVariable("id") Long clazzId) {
        NoContentResponse response = clazzService.deleteClass(clazzId);
        return clazzConverter.getResponse(response);
    }

}
