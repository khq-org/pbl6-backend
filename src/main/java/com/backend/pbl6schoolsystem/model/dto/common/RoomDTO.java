package com.backend.pbl6schoolsystem.model.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomDTO implements Serializable {
    private Long roomId;
    private String room;
    private Long schoolId;
}
