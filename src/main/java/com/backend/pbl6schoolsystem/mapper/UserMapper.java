package com.backend.pbl6schoolsystem.mapper;

import com.backend.pbl6schoolsystem.model.dto.common.UserDTO;
import com.backend.pbl6schoolsystem.model.entity.UserEntity;
import com.backend.pbl6schoolsystem.util.RequestUtil;

public class UserMapper {
    public static UserDTO entity2dto(UserEntity entity) {
        UserDTO dto = new UserDTO();
        dto.setUserId(entity.getUserId());
        dto.setUsername(entity.getUsername());
        dto.setLastName(entity.getLastName());
        dto.setMiddleName(entity.getMiddleName());
        dto.setFirstName(entity.getFirstName());
        dto.setDisplayName(entity.getLastName() + " " + entity.getMiddleName() + " " + entity.getFirstName());
        dto.setEmail(entity.getEmail());
        dto.setPhone(RequestUtil.blankIfNull(entity.getPhone()));
        dto.setStatus(entity.getStatus().getStatus());
        dto.setSchoolId(entity.getSchool().getSchoolId());
        dto.setSchoolName(entity.getSchool().getSchool());
        dto.setStreet(RequestUtil.blankIfNull(entity.getStreet()));
        dto.setDistrict(RequestUtil.blankIfNull(entity.getDistrict()));
        dto.setCity(RequestUtil.blankIfNull(entity.getCity()));
        dto.setDateOfBirth(entity.getDateOfBirth());
        dto.setPlaceOfBirth(RequestUtil.blankIfNull(entity.getPlaceOfBirth()));
        return dto;
    }

}
