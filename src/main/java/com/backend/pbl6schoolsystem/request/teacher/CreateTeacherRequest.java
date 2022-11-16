package com.backend.pbl6schoolsystem.request.teacher;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTeacherRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String dateOfBirth;
    private String street;
    private String district;
    private String city;
    private String workingPosition;
    private Boolean gender;
    private String nationality;
}
