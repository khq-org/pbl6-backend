package com.backend.pbl6schoolsystem.model.dto.calendar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "set")
public class CalendarEventDTO implements Serializable {
    private Long calendarEventId;
    private String calendarEvent;
    private String calendarEventType;
    private Integer lessonStart;
    private Integer lessonFinish;
    private LocalTime timeStart;
    private LocalTime timeFinish;
    private Long roomId;
    private String roomName;
    private Long subjectId;
    private String subjectName;
    private LocalDate calendarDate;
    private String dayOfWeek;
}
