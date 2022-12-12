package com.backend.pbl6schoolsystem.common.enums;

public enum ExamType {
    A1(1L, "A1"),
    A2(2L, "A2"),
    A3(3L, "A3"),
    B1(4L, "B1"),

    B2(5L, "B2"),

    B3(6L, "B3"),

    B4(7L, "B4"),
    C1(8L, "C1"),
    D1(9L, "D1"),
    D2(10L, "D2"),
    D3(11L, "D3"),
    D4(12L, "D4"),
    E1(12L, "E1");

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
