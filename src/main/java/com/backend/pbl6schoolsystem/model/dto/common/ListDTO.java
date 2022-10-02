package com.backend.pbl6schoolsystem.model.dto.common;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder(setterPrefix = "set")
public class ListDTO<T> implements Serializable {
    private Long totalItems;
    private List<T> items;
}
