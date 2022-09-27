package com.backend.pbl6schoolsystem.model.dto.common;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
public class PageDTO<T> implements Serializable {
    private Integer page;
    private Integer size;
    private Integer totalPages;
    private Long totalItems;
    private List<T> items;
}
