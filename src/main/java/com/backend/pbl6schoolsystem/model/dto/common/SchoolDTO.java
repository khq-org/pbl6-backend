package com.backend.pbl6schoolsystem.model.dto.common;

import com.backend.pbl6schoolsystem.model.dto.common.DistrictDTO;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchoolDTO implements Serializable {
    private Long schoolId;
    private String school;
    private String phone;
    private String schoolType;
    private String street;
    private String website;
    private DistrictDTO district;
}
