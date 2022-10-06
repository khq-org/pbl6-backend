package com.backend.pbl6schoolsystem.mapper;

import com.backend.pbl6schoolsystem.model.dto.common.ClazzDTO;
import com.backend.pbl6schoolsystem.model.dto.common.GradeDTO;
import com.backend.pbl6schoolsystem.model.entity.ClassEntity;

public class ClazzMapper {
    public static ClazzDTO entity2dto(ClassEntity classEntity) {
        ClazzDTO dto = new ClazzDTO();
        dto.setClassId(classEntity.getClassId());
        dto.setClazz(classEntity.getClazz());
        dto.setGrade(GradeDTO.builder()
                .setGradeId(classEntity.getGrade().getGradeId())
                .setGrade(classEntity.getGrade().getGrade())
                .build());
        dto.setTeacher(ClazzDTO.Teacher.builder()
                .setTeacherId(classEntity.getTeacher().getUserId())
                .setTeacher(classEntity.getTeacher().getFirstName() + " " + classEntity.getTeacher().getLastName())
                .build());
        return dto;
    }
}
