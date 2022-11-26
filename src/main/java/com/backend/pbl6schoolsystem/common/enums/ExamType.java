package com.backend.pbl6schoolsystem.common.enums;

public enum ExamType {
    TYPE_I(1L, "Type I"),
    TYPE_II(2L, "Type II"),
    TYPE_III(2L, "Type III"),
    TYPE_IV(2L, "Type IV");
    private Long id;
    private String name;

    ExamType(Long id, String name) {
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
