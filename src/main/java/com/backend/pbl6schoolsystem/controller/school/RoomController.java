package com.backend.pbl6schoolsystem.controller.school;

import com.backend.pbl6schoolsystem.converter.RoomConverter;
import com.backend.pbl6schoolsystem.model.dto.common.ListDTO;
import com.backend.pbl6schoolsystem.model.dto.common.RoomDTO;
import com.backend.pbl6schoolsystem.response.Response;
import com.backend.pbl6schoolsystem.response.room.ListRoomResponse;
import com.backend.pbl6schoolsystem.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Room", description = "Room APIs")
@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;
    private final RoomConverter roomConverter;

    @Operation(summary = "Get list room in school")
    @GetMapping
    public Response<ListDTO<RoomDTO>> getListRoom() {
        ListRoomResponse response = roomService.getListRoom();
        return roomConverter.getResponse(response);
    }
}
