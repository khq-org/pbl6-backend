package com.backend.pbl6schoolsystem.converter;

import com.backend.pbl6schoolsystem.model.dto.common.ListDTO;
import com.backend.pbl6schoolsystem.model.dto.common.SchoolYearDTO;
import com.backend.pbl6schoolsystem.response.Response;
import com.backend.pbl6schoolsystem.response.schoolyear.ListSchoolYearResponse;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class SchoolYearConverter extends CommonConverter {
    public Response<ListDTO<SchoolYearDTO>> getResponse(ListSchoolYearResponse response) {
        return Response.<ListDTO<SchoolYearDTO>>builder()
                .setSuccess(true)
                .setData(ListDTO.<SchoolYearDTO>builder()
                        .setTotalItems((long) response.getItems().size())
                        .setItems(response.getItems())
                        .build())
                .setTimestamp(new Timestamp(System.currentTimeMillis()))
                .build();
    }
}
