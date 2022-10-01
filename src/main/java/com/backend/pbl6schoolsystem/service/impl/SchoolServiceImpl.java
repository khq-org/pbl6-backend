package com.backend.pbl6schoolsystem.service.impl;

import com.backend.pbl6schoolsystem.common.exception.BadRequestException;
import com.backend.pbl6schoolsystem.mapper.SchoolMapper;
import com.backend.pbl6schoolsystem.model.dto.common.SchoolDTO;
import com.backend.pbl6schoolsystem.model.entity.SchoolEntity;
import com.backend.pbl6schoolsystem.repository.dsl.SchoolDslRepository;
import com.backend.pbl6schoolsystem.repository.jpa.SchoolRepository;
import com.backend.pbl6schoolsystem.response.PageResponse;
import com.backend.pbl6schoolsystem.util.RequestUtil;
import com.backend.pbl6schoolsystem.request.school.CreateSchoolRequest;
import com.backend.pbl6schoolsystem.request.school.ListSchoolRequest;
import com.backend.pbl6schoolsystem.response.OnlyIdResponse;
import com.backend.pbl6schoolsystem.response.school.ListSchoolResponse;
import com.backend.pbl6schoolsystem.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchoolServiceImpl implements SchoolService {
    private final SchoolDslRepository schoolDslRepository;
    private final SchoolRepository schoolRepository;

    @Override
    public OnlyIdResponse createSchool(CreateSchoolRequest schoolRequest) {
        boolean isExistSchoolInCity = schoolRepository.findSchoolByCity(schoolRequest.getCity()).isPresent();
        if (isExistSchoolInCity) {
            throw new BadRequestException("School is already exist in city!");
        }
        SchoolEntity schoolEntity = schoolRepository.save(school2entity(schoolRequest));
        return OnlyIdResponse.builder()
                .setId(schoolEntity.getSchoolId())
                .setName(schoolEntity.getSchool())
                .build();
    }

    @Override
    public ListSchoolResponse getListSchool(ListSchoolRequest request) {
        List<SchoolEntity> schoolEntities = schoolDslRepository.getListSchool(request);
        return ListSchoolResponse.builder()
                .setPageResponse(PageResponse.builder()
                        .setTotalItems((long) schoolEntities.size())
                        .setPage(request.getPageRequest().getPage())
                        .setSize(request.getPageRequest().getAll() ? Integer.MAX_VALUE : request.getPageRequest().getSize())
                        .setTotalPages(request.getPageRequest().getAll() ? 1 : RequestUtil.getTotalPages((long) schoolEntities.size(), request.getPageRequest().getSize()))
                        .build())
                .setItems(schoolEntities.stream()
                        .map(se -> SchoolMapper.dto2entity(se))
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
}
