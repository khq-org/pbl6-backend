package com.backend.pbl6schoolsystem.request.teacher;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "set")
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
    private Long subjectId;
    private Boolean gender;
    private String nationality;
}
