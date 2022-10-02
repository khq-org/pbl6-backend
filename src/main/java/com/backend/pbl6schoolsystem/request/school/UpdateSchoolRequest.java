package com.backend.pbl6schoolsystem.request.school;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateSchoolRequest {
    private String school;
    private String phone;
    private String schoolType;
    private String street;
    private String district;
    private String city;
    private String website;
}
