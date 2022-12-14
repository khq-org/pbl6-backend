package com.backend.pbl6schoolsystem.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(setterPrefix = "set")
public class PageResponse {
    private Integer size;
    private Integer page;
    private Long totalItems;
    private Integer totalPages;
}
