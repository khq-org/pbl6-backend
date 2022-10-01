package com.backend.pbl6schoolsystem.mapper;

import com.backend.pbl6schoolsystem.model.dto.common.SchoolDTO;
import com.backend.pbl6schoolsystem.model.entity.SchoolEntity;

public class SchoolMapper {
    public static SchoolDTO dto2entity(SchoolEntity entity) {
        SchoolDTO dto = new SchoolDTO();
        dto.setSchoolId(entity.getSchoolId());
        dto.setSchool(entity.getSchool());
        dto.setStreet(entity.getStreet());
        dto.setDistrict(entity.getDistrict());
        dto.setCity(entity.getCity());
        dto.setSchoolType(entity.getSchoolType());
        dto.setWebsite(entity.getWebsite());
        return dto;
    }

}
