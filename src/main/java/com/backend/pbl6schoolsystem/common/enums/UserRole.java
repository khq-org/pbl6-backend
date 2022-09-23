package com.backend.pbl6schoolsystem.common.enums;

public enum UserRole {
    ADMIN_ROLE(1L, "ADMIN"),
    SCHOOL_ROLE(2L, "SCHOOL"),
    TEACHER_ROLE(3L, "TEACHER"),
    STUDENT_ROLE(4L, "STUDENT");
    private Long roleId;
    private String role;

    UserRole(Long roleId, String role) {
        this.roleId = roleId;
        this.role = role;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getRoleId() {
        return this.roleId;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return this.role;
    }
}
