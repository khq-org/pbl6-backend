package com.backend.pbl6schoolsystem.mapper;

import com.backend.pbl6schoolsystem.model.dto.common.UserDTO;
import com.backend.pbl6schoolsystem.model.entity.UserEntity;
import com.backend.pbl6schoolsystem.util.RequestUtil;

public class UserMapper {
    public static UserDTO entity2dto(UserEntity entity) {
        UserDTO dto = new UserDTO();
        dto.setUserId(entity.getUserId());
        dto.setUsername(RequestUtil.blankIfNull(entity.getUsername()));
        dto.setLastName(entity.getLastName());
        dto.setMiddleName(RequestUtil.blankIfNull(entity.getMiddleName()));
        dto.setFirstName(entity.getFirstName());
        dto.setDisplayName(entity.getLastName() + " " + entity.getMiddleName() + " " + entity.getFirstName());
        dto.setEmail(RequestUtil.blankIfNull(entity.getEmail()));
        dto.setPhone(RequestUtil.blankIfNull(entity.getPhone()));
        if (entity.getRole() != null) {
            dto.setRole(entity.getRole().getRole());
        }
        dto.setJob(RequestUtil.blankIfNull(entity.getJob()));
        if (entity.getSchool() != null) {
            dto.setSchoolId(entity.getSchool().getSchoolId());
            dto.setSchoolName(entity.getSchool().getSchool());
        }
        dto.setStreet(RequestUtil.blankIfNull(entity.getStreet()));
        dto.setDistrict(RequestUtil.blankIfNull(entity.getDistrict()));
        dto.setCity(RequestUtil.blankIfNull(entity.getCity()));
        dto.setPlaceOfBirth(RequestUtil.blankIfNull(entity.getPlaceOfBirth()));
        if (entity.getDateOfBirth() != null) {
            dto.setDateOfBirth(entity.getDateOfBirth());
        }
        dto.setStudentId(RequestUtil.blankIfNull(entity.getStudentId()));
        return dto;
    }

}
