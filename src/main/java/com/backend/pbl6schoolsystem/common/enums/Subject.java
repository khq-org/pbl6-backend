package com.backend.pbl6schoolsystem.common.enums;

public enum Subject {
    MATHS(1L, "Maths"),
    PHYSIC(2L, "Physic"),
    CHEMISTRY(3L, "Chemistry"),
    LITERATURE(4L, "Literature"),
    HISTORY(5L, "History"),
    GEOGRAPHIC(6L, "Geographic"),
    BIOLOGICAL(7L, "Biological"),
    ENGLISH(8L, "English"),
    CIVIC_EDUCATION(9L, "Civic Education"),
    TECHNOLOGY(10L, "Technology"),
    DEFENSE_EDUCATION(11L, "Defense Education"),
    PHYSICAL_EDUCATION(12L, "Physical Education"),
    INFORMATICS(13L, "Informatics");

    private Long id;
    private String name;

    Subject(Long id, String name) {
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
