package com.backend.pbl6schoolsystem.model.dto.common;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DistrictDTO implements Serializable {
    private Long districtId;
    private String district;
    private String city;
    private String zipCode;
}
