package com.backend.pbl6schoolsystem.common.enums;

public enum Grade {
    GRADE_10(1L, "GRADE 10"),
    GRADE_11(2L, "GRADE 11"),
    GRADE_12(3L, "GRADE 12");

    private Long gradeId;
    private String grade;

    Grade(Long gradeId, String grade) {
        this.gradeId = gradeId;
        this.grade = grade;
    }

    public void setGradeId(Long gradeId) {
        this.gradeId = gradeId;
    }

    public Long getGradeId() {
        return this.gradeId;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getGrade() {
        return this.grade;
    }
}
