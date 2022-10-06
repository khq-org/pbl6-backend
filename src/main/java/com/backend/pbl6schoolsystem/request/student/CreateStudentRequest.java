package com.backend.pbl6schoolsystem.request.student;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "set")
public class CreateStudentRequest {
    private String firstName;
    private String lastName;
    private String middleName;
    private String phone;
    private String email;
    private String street;
    private String district;
    private String city;
    private Long schoolId;
    private List<Parent> parents;
    private Boolean notHaveParents;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(setterPrefix = "set")
    public static class Parent {
        private String firstName;
        private String lastName;
        private String middleName;
        private String phone;
        private String job;
        private String street;
        private String district;
        private String city;
    }

}
