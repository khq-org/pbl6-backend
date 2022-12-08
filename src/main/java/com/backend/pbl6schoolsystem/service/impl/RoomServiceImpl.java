package com.backend.pbl6schoolsystem.service.impl;

import com.backend.pbl6schoolsystem.model.dto.common.RoomDTO;
import com.backend.pbl6schoolsystem.model.entity.RoomEntity;
import com.backend.pbl6schoolsystem.repository.jpa.RoomRepository;
import com.backend.pbl6schoolsystem.response.room.ListRoomResponse;
import com.backend.pbl6schoolsystem.security.UserPrincipal;
import com.backend.pbl6schoolsystem.service.RoomService;
import com.backend.pbl6schoolsystem.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;

    @Override
    public ListRoomResponse getListRoom() {
        UserPrincipal principal = SecurityUtils.getPrincipal();
        List<RoomEntity> rooms = roomRepository.findBySchoolId(principal.getSchoolId());
        return ListRoomResponse.builder()
                .setSuccess(true)
                .setItems(rooms.stream()
                        .map(r -> RoomDTO.builder()
                                .roomId(r.getRoomId())
                                .room(r.getRoom())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
