package com.backend.pbl6schoolsystem.security;

import com.backend.pbl6schoolsystem.common.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
public class UserPrincipal implements Serializable {
    private Long userId;
    private String firstName;
    private String lastName;
    private String street;
    private String district;
    private String city;
    private String role;
    private Long schoolId;
    private Timestamp createdDate;

    public Boolean isSchoolAdmin() {
        return UserRole.SCHOOL_ROLE.getRole().equalsIgnoreCase(role);
    }

    public Boolean isStudent() {
        return UserRole.STUDENT_ROLE.getRole().equalsIgnoreCase(role);
    }

    public Boolean isTeacher() {
        return UserRole.TEACHER_ROLE.getRole().equalsIgnoreCase(role);
    }
}
