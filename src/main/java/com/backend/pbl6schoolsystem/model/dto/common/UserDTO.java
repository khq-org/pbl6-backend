package com.backend.pbl6schoolsystem.model.dto.common;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO implements Serializable {
    private Long userId;
    private String username;
    private String password;
    private String lastName;
    private String firstName;
    private String middleName;
    private String displayName;
    private String phone;
    private String email;
    private String avatar;
    private Boolean gender;
    private LocalDate dateOfBirth;
    private String placeOfBirth;
    private String nationality;
    private String street;
    private String district;
    private String city;
    private LocalDate recruitmentDay;
    private String workingPosition;
    private String positionGroup;
    private Double factorSalary;
    private Integer rank; // rank 1,2,3,4
    private Integer level; // level 1,2,3,4
    private String role;
    private String status;
    private Long schoolId;
    private String schoolName;
}
