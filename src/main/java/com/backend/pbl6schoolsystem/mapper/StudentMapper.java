package com.backend.pbl6schoolsystem.mapper;

import com.backend.pbl6schoolsystem.model.dto.student.StudentDTO;
import com.backend.pbl6schoolsystem.model.dto.student.StudentDetailDTO;
import com.backend.pbl6schoolsystem.model.entity.UserEntity;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class StudentMapper {
    public static StudentDTO entity2dto(UserEntity userEntity) {
        StudentDTO dto = new StudentDTO();
        dto.setUserDTO(UserMapper.entity2dto(userEntity));
        dto.setStudentId(userEntity.getStudentId());
        if (userEntity.getClazz() != null) {
            dto.setClazz(StudentDTO.Clazz.builder()
                    .setClazzId(userEntity.getClazz().getClassId())
                    .setClazz(userEntity.getClazz().getClazz())
                    .build());
        }
        return dto;
    }

    public static StudentDetailDTO entity2DetailDto(UserEntity userEntity, List<UserEntity> parents) {
        StudentDetailDTO dto = new StudentDetailDTO();
        dto.setUserDTO(UserMapper.entity2dto(userEntity));
        if (userEntity.getClazz() != null) {
            dto.setClazz(ClazzMapper.entity2dto(userEntity.getClazz()));
        }
        dto.setParents(parents.stream()
                .map(p -> StudentDetailDTO.Parent.builder()
                        .setUserDTO(UserMapper.entity2dto(p))
                        .build())
                .collect(Collectors.toList()));
        return dto;
    }

}
