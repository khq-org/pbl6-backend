package com.backend.pbl6schoolsystem.converter;

import com.backend.pbl6schoolsystem.model.dto.common.ListDTO;
import com.backend.pbl6schoolsystem.model.dto.common.SubjectDTO;
import com.backend.pbl6schoolsystem.response.Response;
import com.backend.pbl6schoolsystem.response.subject.GetListSubjectResponse;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class SubjectConverter extends CommonConverter {

    public Response<ListDTO<SubjectDTO>> getResponse(GetListSubjectResponse response) {
        return Response.<ListDTO<SubjectDTO>>builder()
                .setSuccess(true)
                .setData(ListDTO.<SubjectDTO>builder()
                        .setTotalItems((long) response.getItems().size())
                        .setItems(response.getItems())
                        .build())
                .setTimestamp(new Timestamp(System.currentTimeMillis()))
                .build();
    }
}
