package com.backend.pbl6schoolsystem.model.dto.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder(setterPrefix = "set")
public class ErrorDTO implements Serializable {
    private String key;
    private String value;
}
