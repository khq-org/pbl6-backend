package com.backend.pbl6schoolsystem.model.dto.student;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "set")
public class ProfileStudentDTO {
    private Long profileStudentId;
    private Student student;
    private List<LearningResultDTO> learningResults;

    @Getter
    @Setter
    @Builder(setterPrefix = "set")
    public static class Student {
        private Long studentId;
        private String firstName;
        private String lastName;
        private String dayOfBirth;
        private String placeOfBirth;
        private String street;
        private String district;
        private String city;
    }

}
