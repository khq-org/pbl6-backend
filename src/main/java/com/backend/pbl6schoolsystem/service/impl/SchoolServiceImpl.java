package com.backend.pbl6schoolsystem.service.impl;

import com.backend.pbl6schoolsystem.common.constant.ErrorCode;
import com.backend.pbl6schoolsystem.common.exception.NotFoundException;
import com.backend.pbl6schoolsystem.mapper.SchoolMapper;
import com.backend.pbl6schoolsystem.model.entity.SchoolEntity;
import com.backend.pbl6schoolsystem.repository.dsl.SchoolDslRepository;
import com.backend.pbl6schoolsystem.repository.jpa.SchoolRepository;
import com.backend.pbl6schoolsystem.request.school.UpdateSchoolRequest;
import com.backend.pbl6schoolsystem.response.ErrorResponse;
import com.backend.pbl6schoolsystem.response.NoContentResponse;
import com.backend.pbl6schoolsystem.response.PageResponse;
import com.backend.pbl6schoolsystem.response.school.GetSchoolResponse;
import com.backend.pbl6schoolsystem.util.RequestUtil;
import com.backend.pbl6schoolsystem.request.school.CreateSchoolRequest;
import com.backend.pbl6schoolsystem.request.school.ListSchoolRequest;
import com.backend.pbl6schoolsystem.response.OnlyIdResponse;
import com.backend.pbl6schoolsystem.response.school.ListSchoolResponse;
import com.backend.pbl6schoolsystem.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchoolServiceImpl implements SchoolService {
    private final SchoolDslRepository schoolDslRepository;
    private final SchoolRepository schoolRepository;

    @Override
    public OnlyIdResponse createSchool(CreateSchoolRequest request) {
        boolean isExistSchoolInCity = schoolRepository.findSchoolByNameAndCity(request.getSchool(),
                request.getCity()).isPresent();
        if (isExistSchoolInCity) {
            Map<String, String> errors = new HashMap<>();
            errors.put("School name", ErrorCode.ALREADY_EXIST.name() + " in city");
            return OnlyIdResponse.builder()
                    .setSuccess(false)
                    .setErrorResponse(ErrorResponse.builder()
                            .setErrors(errors)
                            .build())
                    .build();
        }
        SchoolEntity schoolEntity = schoolRepository.save(school2entity(request));
        return OnlyIdResponse.builder()
                .setSuccess(true)
                .setId(schoolEntity.getSchoolId())
                .setName(schoolEntity.getSchool())
                .build();
    }

    @Override
    public OnlyIdResponse updateSchool(Long schoolId, UpdateSchoolRequest request) {
        boolean isExistSchoolInCity = schoolRepository.findSchoolByNameAndCity(request.getSchool(),
                request.getCity()).isPresent();
        if (isExistSchoolInCity) {
            Map<String, String> errors = new HashMap<>();
            errors.put("School name", ErrorCode.ALREADY_EXIST.name() + " in city");
            return OnlyIdResponse.builder()
                    .setSuccess(false)
                    .setErrorResponse(ErrorResponse.builder()
                            .setErrors(errors)
                            .build())
                    .build();
        } else {
            SchoolEntity school = schoolRepository.findById(schoolId).orElseThrow(() -> new NotFoundException("Not found school " + schoolId));
            school = updateEntity(school, request);
            schoolRepository.save(school);
            return OnlyIdResponse.builder()
                    .setSuccess(true)
                    .setId(school.getSchoolId())
                    .setName(school.getSchool())
                    .build();
        }
    }

    @Override
    public NoContentResponse deleteSchool(Long schoolId) {
        SchoolEntity school = schoolRepository.findById(schoolId).orElseThrow(() -> new NotFoundException("Not found school with id " + schoolId));
        schoolRepository.delete(school);
        return NoContentResponse.builder()
                .setSuccess(true)
                .build();
    }

    @Override
    public GetSchoolResponse getSchool(Long schoolId) {
        SchoolEntity school = schoolRepository.findById(schoolId).orElseThrow(() -> new NotFoundException("Not found school with id " + schoolId));
        return GetSchoolResponse.builder()
                .setSuccess(true)
                .setSchoolDTO(SchoolMapper.entity2dto(school))
                .build();
    }

    @Override
    public ListSchoolResponse getListSchool(ListSchoolRequest request) {
        List<SchoolEntity> schoolEntities = schoolDslRepository.getListSchool(request);
        return ListSchoolResponse.builder()
                .setPageResponse(PageResponse.builder()
                        .setTotalItems((long) schoolEntities.size())
                        .setPage(request.getPage())
                        .setSize(request.getAll() ? Integer.MAX_VALUE : request.getSize())
                        .setTotalPages(request.getAll() ? 1 : RequestUtil.getTotalPages((long) schoolEntities.size(), request.getSize()))
                        .build())
                .setItems(schoolEntities.stream()
                        .map(se -> SchoolMapper.entity2dto(se))
                        .collect(Collectors.toList()))
                .build();
    }

    public SchoolEntity school2entity(CreateSchoolRequest schoolRequest) {
        SchoolEntity schoolEntity = new SchoolEntity();
        schoolEntity.setSchool(schoolRequest.getSchool());
        schoolEntity.setSchoolType(schoolRequest.getSchoolType());
        schoolEntity.setStreet(schoolRequest.getStreet());
        schoolEntity.setDistrict(schoolRequest.getDistrict());
        schoolEntity.setCity(schoolRequest.getCity());
        schoolEntity.setPhone(RequestUtil.blankIfNull(schoolRequest.getPhone()));
        schoolEntity.setWebsite(RequestUtil.blankIfNull(schoolRequest.getWebsite()));
        schoolEntity.setModifiedDate(new Timestamp(System.currentTimeMillis()));
        return schoolEntity;
    }

    public SchoolEntity updateEntity(SchoolEntity entity, UpdateSchoolRequest request) {
        entity.setSchool(RequestUtil.blankIfNull(request.getSchool()));
        entity.setSchoolType(RequestUtil.blankIfNull(request.getSchoolType()));
        entity.setStreet(RequestUtil.blankIfNull(request.getStreet()));
        entity.setDistrict(RequestUtil.blankIfNull(request.getDistrict()));
        entity.setCity(RequestUtil.blankIfNull(request.getCity()));
        entity.setWebsite(RequestUtil.blankIfNull(request.getWebsite()));
        entity.setModifiedDate(new Timestamp(System.currentTimeMillis()));
        return entity;
    }
}
