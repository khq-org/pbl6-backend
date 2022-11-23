package com.backend.pbl6schoolsystem.converter;

import com.backend.pbl6schoolsystem.model.dto.common.ClazzDTO;
import com.backend.pbl6schoolsystem.model.dto.common.GradeDTO;
import com.backend.pbl6schoolsystem.model.dto.common.ListDTO;
import com.backend.pbl6schoolsystem.response.Response;
import com.backend.pbl6schoolsystem.response.clazz.GetClassResponse;
import com.backend.pbl6schoolsystem.response.clazz.ListClassResponse;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.stream.Collectors;

@Component
public class ClazzConverter extends CommonConverter {
    public Response<ListDTO<ClazzDTO>> getResponse(ListClassResponse response) {
        return Response.<ListDTO<ClazzDTO>>builder()
                .setSuccess(true)
                .setData(ListDTO.<ClazzDTO>builder()
                        .setTotalItems((long) response.getItems().size())
                        .setItems(response.getItems())
                        .build())
                .setTimestamp(new Timestamp(System.currentTimeMillis()))
                .build();
    }

    public Response<ClazzDTO> getResponse(GetClassResponse response) {
        return Response.<ClazzDTO>builder()
                .setSuccess(true)
                .setData(response.getClazzDTO())
                .setTimestamp(new Timestamp(System.currentTimeMillis()))
                .build();
    }
}
