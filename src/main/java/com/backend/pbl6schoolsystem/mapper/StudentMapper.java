package com.backend.pbl6schoolsystem.mapper;

import com.backend.pbl6schoolsystem.model.dto.student.StudentDTO;
import com.backend.pbl6schoolsystem.model.entity.UserEntity;

public class StudentMapper {
    public static StudentDTO entity2dto(UserEntity userEntity) {
        StudentDTO dto = new StudentDTO();
        dto.setUserDTO(UserMapper.entity2dto(userEntity));
        dto.setClazz(StudentDTO.Clazz.builder()
                        .setClazzId(userEntity.getClazz().getClassId())
                        .setClazz(userEntity.getClazz().getClazz())
                .build());
        return dto;
    }
}
