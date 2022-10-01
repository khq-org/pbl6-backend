package com.backend.pbl6schoolsystem.controller;

import com.backend.pbl6schoolsystem.model.dto.common.OnlyIdDTO;
import com.backend.pbl6schoolsystem.request.school.CreateSchoolRequest;
import com.backend.pbl6schoolsystem.response.OnlyIdResponse;
import com.backend.pbl6schoolsystem.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Response;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SchoolController {
    private final SchoolService schoolService;

    @PostMapping("/schools")
    public ResponseEntity<OnlyIdDTO> createSchool(@RequestBody CreateSchoolRequest request) {
        OnlyIdResponse response = schoolService.createSchool(request);
        OnlyIdDTO onlyIdDTO = OnlyIdDTO.builder()
                .setId(response.getId())
                .setName(response.getName())
                .build();
        return new ResponseEntity<>(onlyIdDTO, HttpStatus.OK);
    }

}
