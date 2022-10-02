package com.backend.pbl6schoolsystem.request.school;

import com.backend.pbl6schoolsystem.request.PageRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListSchoolRequest extends PageRequest {
    private String schoolName;
    private String district;
    private String city;
    private String schoolType;
}
