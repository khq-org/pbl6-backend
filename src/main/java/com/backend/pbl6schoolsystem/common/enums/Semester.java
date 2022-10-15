package com.backend.pbl6schoolsystem.common.enums;

public enum Semester {
    SEMESTER_I(1L, "Semester I"),
    SEMESTER_II(2L, "Semester II");
    private Long id;
    private String name;

    Semester(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
