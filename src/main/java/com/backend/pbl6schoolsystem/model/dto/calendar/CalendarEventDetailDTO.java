package com.backend.pbl6schoolsystem.model.dto.calendar;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder(setterPrefix = "set")
@AllArgsConstructor
@NoArgsConstructor
public class CalendarEventDetailDTO {
    private CalendarEventDTO calendarEvent;
    private List<Clazz> classes;
    private List<User> users;

    @Getter
    @Setter
    @Builder(setterPrefix = "set")
    public static class Clazz {
        private Long clazzId;
        private String clazz;
        private String grade;
    }

    @Getter
    @Setter
    @Builder(setterPrefix = "set")
    public static class User {
        private Long userId;
        private String firstName;
        private String lastName;
        private String displayName;
        private Long clazzId;
        private String clazz;
        private String workingPosition; // for teacher
    }
}
