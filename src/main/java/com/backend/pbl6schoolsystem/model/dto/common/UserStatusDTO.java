package com.backend.pbl6schoolsystem.model.dto.common;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStatusDTO implements Serializable {
    private Long statusId;
    private String status;
}
