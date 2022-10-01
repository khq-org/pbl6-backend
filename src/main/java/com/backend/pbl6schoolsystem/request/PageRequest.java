package com.backend.pbl6schoolsystem.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageRequest {
    private Integer page;
    private Integer size;
    private String sort = "name";
    private String direction = "asc";
    private Boolean all = true;
}
