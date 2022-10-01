package com.backend.pbl6schoolsystem.model.dto.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(setterPrefix = "set")
public class OnlyIdDTO {
    private Long id;
    private String name;
}
