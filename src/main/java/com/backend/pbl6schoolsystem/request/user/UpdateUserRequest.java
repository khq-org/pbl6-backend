package com.backend.pbl6schoolsystem.request.user;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "set")
public class UpdateUserRequest {
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String street;
    private String district;
    private String city;
    private LocalDate dateOfBirth;
    private String placeOfBirth;
    private String nationality;
    private String workingPosition;
    private Long roleId;
}
