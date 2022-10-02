package com.backend.pbl6schoolsystem.converter;

import com.backend.pbl6schoolsystem.model.dto.common.ListDTO;
import com.backend.pbl6schoolsystem.model.dto.common.MessageDTO;
import com.backend.pbl6schoolsystem.model.dto.common.OnlyIdDTO;
import com.backend.pbl6schoolsystem.model.dto.common.SchoolDTO;
import com.backend.pbl6schoolsystem.model.entity.SchoolEntity;
import com.backend.pbl6schoolsystem.response.NoContentResponse;
import com.backend.pbl6schoolsystem.response.OnlyIdResponse;
import com.backend.pbl6schoolsystem.response.Response;
import com.backend.pbl6schoolsystem.response.school.GetSchoolResponse;
import com.backend.pbl6schoolsystem.response.school.ListSchoolResponse;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.stream.Collectors;

@Component
public class SchoolConverter extends CommonConverter {
    public Response<ListDTO<SchoolDTO>> getResponse(ListSchoolResponse response) {

        return Response.<ListDTO<SchoolDTO>>builder()
                .setSuccess(true)
                .setData(ListDTO.<SchoolDTO>builder()
                        .setTotalItems((long) response.getItems().size())
                        .setItems(response.getItems().stream()
                                .map(s -> SchoolDTO.builder()
                                        .setSchoolId(s.getSchoolId())
                                        .setSchool(s.getSchool())
                                        .setSchoolType(s.getSchoolType())
                                        .setStreet(s.getStreet())
                                        .setDistrict(s.getDistrict())
                                        .setCity(s.getCity())
                                        .setPhone(s.getPhone())
                                        .setWebsite(s.getWebsite())
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .setTimestamp(new Timestamp(System.currentTimeMillis()))
                .build();

    }

    public Response<SchoolDTO> getResponse(SchoolEntity schoolEntity) {
        return Response.<SchoolDTO>builder()
                .setSuccess(true)
                .setData(SchoolDTO.builder()
                        .setSchoolId(schoolEntity.getSchoolId())
                        .setSchoolType(schoolEntity.getSchoolType())
                        .setStreet(schoolEntity.getStreet())
                        .setDistrict(schoolEntity.getDistrict())
                        .setCity(schoolEntity.getCity())
                        .setPhone(schoolEntity.getPhone())
                        .setWebsite(schoolEntity.getWebsite())
                        .build())
                .setTimestamp(new Timestamp(System.currentTimeMillis()))
                .build();
    }

    public Response<OnlyIdDTO> getResponse(OnlyIdResponse response) {
        return Response.<OnlyIdDTO>builder()
                .setSuccess(true)
                .setData(OnlyIdDTO.builder()
                        .setId(response.getId())
                        .setName(response.getName())
                        .build())
                .setTimestamp(new Timestamp(System.currentTimeMillis()))
                .build();
    }

    public Response<MessageDTO> getResponse(NoContentResponse response) {
        return Response.<MessageDTO>builder()
                .setSuccess(true)
                .setMessage("Deleted")
                .setTimestamp(new Timestamp(System.currentTimeMillis()))
                .build();
    }

    public Response<SchoolDTO> getResponse(GetSchoolResponse response) {
        return Response.<SchoolDTO>builder()
                .setSuccess(true)
                .setData(response.getSchoolDTO())
                .setTimestamp(new Timestamp(System.currentTimeMillis()))
                .build();
    }
}
