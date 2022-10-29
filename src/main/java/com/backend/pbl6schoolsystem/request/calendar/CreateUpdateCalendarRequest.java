package com.backend.pbl6schoolsystem.request.calendar;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateUpdateCalendarRequest {
    private String calendarEventName;
    @Schema(example = "Study, Examination, ...")
    private String calendarEventType;
    private List<Long> classIds;
    private List<Long> userIds;
    @Schema(example = "07:00")
    private String timeStart;
    @Schema(example = "08:45")
    private String timeFinish;
    @Schema(example = "2022-10-20")
    private String date;
    @Schema(description = "Monday -> Saturday", example = "Tuesday")
    private String dayOfWeek;
    @Schema(description = "Lesson 1 -> 5", example = "4")
    private Integer lessonStart;
    @Schema(description = "Lesson 1 -> 5", example = "5")
    private Integer lessonFinish;
}
