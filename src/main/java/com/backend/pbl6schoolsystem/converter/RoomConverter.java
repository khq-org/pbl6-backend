package com.backend.pbl6schoolsystem.converter;

import com.backend.pbl6schoolsystem.model.dto.common.ListDTO;
import com.backend.pbl6schoolsystem.model.dto.common.RoomDTO;
import com.backend.pbl6schoolsystem.response.Response;
import com.backend.pbl6schoolsystem.response.room.ListRoomResponse;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class RoomConverter extends CommonConverter {

    public Response<ListDTO<RoomDTO>> getResponse(ListRoomResponse response) {
        return Response.<ListDTO<RoomDTO>>builder()
                .setSuccess(true)
                .setData(ListDTO.<RoomDTO>builder()
                        .setTotalItems((long) response.getItems().size())
                        .setItems(response.getItems())
                        .build())
                .setTimestamp(new Timestamp(System.currentTimeMillis()))
                .build();
    }
}
