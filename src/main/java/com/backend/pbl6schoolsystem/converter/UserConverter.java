package com.backend.pbl6schoolsystem.converter;

import com.backend.pbl6schoolsystem.model.dto.common.ListDTO;
import com.backend.pbl6schoolsystem.model.dto.common.UserDTO;
import com.backend.pbl6schoolsystem.model.dto.student.StudentDTO;
import com.backend.pbl6schoolsystem.model.dto.user.SchoolAdminDTO;
import com.backend.pbl6schoolsystem.response.user.GetSchoolAdminResponse;
import com.backend.pbl6schoolsystem.response.user.ListUserResponse;
import com.backend.pbl6schoolsystem.response.Response;
import com.backend.pbl6schoolsystem.response.student.GetStudentResponse;
import com.backend.pbl6schoolsystem.response.student.ListStudentResponse;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.stream.Collectors;

@Component
public class UserConverter extends CommonConverter {
    public Response<ListDTO<UserDTO>> getResponse(ListStudentResponse response) {
        return Response.<ListDTO<UserDTO>>builder()
                .setSuccess(true)
                .setData(ListDTO.<UserDTO>builder()
                        .setTotalItems((long) response.getItems().size())
                        .setItems(response.getItems())
                        .build())
                .setTimestamp(new Timestamp(System.currentTimeMillis()))
                .build();
    }

    public Response<StudentDTO> getResponse(GetStudentResponse response) {
        return Response.<StudentDTO>builder()
                .setSuccess(true)
                .setData(response.getStudent())
                .setTimestamp(new Timestamp(System.currentTimeMillis()))
                .build();
    }

    public Response<ListDTO<SchoolAdminDTO>> getResponse(ListUserResponse response) {
        return Response.<ListDTO<SchoolAdminDTO>>builder()
                .setSuccess(true)
                .setData(ListDTO.<SchoolAdminDTO>builder()
                        .setTotalItems((long) response.getItems().size())
                        .setItems(response.getItems().stream()
                                .map(r -> SchoolAdminDTO.builder()
                                        .setSchoolAdminId(r.getUserId())
                                        .setUsername(r.getUsername())
                                        .setFirstName(r.getFirstName())
                                        .setLastName(r.getLastName())
                                        .setEmail(r.getEmail())
                                        .setPassword("Anonymous")
                                        .setSchoolId(r.getSchoolId())
                                        .setSchoolName(r.getSchoolName())
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .setTimestamp(new Timestamp(System.currentTimeMillis()))
                .build();
    }

    public Response<SchoolAdminDTO> getResponse(GetSchoolAdminResponse response) {
        return Response.<SchoolAdminDTO>builder()
                .setSuccess(true)
                .setData(response.getSchoolAdminDTO())
                .setTimestamp(new Timestamp(System.currentTimeMillis()))
                .build();
    }
}
