package com.backend.pbl6schoolsystem.converter;

import com.backend.pbl6schoolsystem.model.dto.common.ListDTO;
import com.backend.pbl6schoolsystem.model.dto.student.StudentDTO;
import com.backend.pbl6schoolsystem.model.dto.student.StudentDetailDTO;
import com.backend.pbl6schoolsystem.response.Response;
import com.backend.pbl6schoolsystem.response.student.GetStudentResponse;
import com.backend.pbl6schoolsystem.response.student.ListStudentResponse;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.stream.Collectors;

@Component
public class UserConverter extends CommonConverter {
    public Response<ListDTO<StudentDTO>> getResponse(ListStudentResponse response) {
        return Response.<ListDTO<StudentDTO>>builder()
                .setSuccess(true)
                .setData(ListDTO.<StudentDTO>builder()
                        .setTotalItems((long) response.getItems().size())
                        .setItems(response.getItems().stream()
                                .map(st -> StudentDTO.builder()
                                        .setStudentId(st.getStudentId())
                                        .setUserDTO(st.getUserDTO())
                                        .setClazz(st.getClazz())
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .setTimestamp(new Timestamp(System.currentTimeMillis()))
                .build();
    }

    public Response<StudentDetailDTO> getResponse(GetStudentResponse response) {
        return Response.<StudentDetailDTO>builder()
                .setSuccess(true)
                .setData(response.getStudentDetailDTO())
                .build();
    }
}
