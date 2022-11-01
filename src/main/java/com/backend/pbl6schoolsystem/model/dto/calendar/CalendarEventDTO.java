package com.backend.pbl6schoolsystem.model.dto.calendar;

import com.backend.pbl6schoolsystem.model.dto.student.StudentDTO;
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
    private String timeStart;
    private String timeFinish;
    private String roomName;
    private String subjectName;
    private String calendarDate;
    private String dayOfWeek;
}
