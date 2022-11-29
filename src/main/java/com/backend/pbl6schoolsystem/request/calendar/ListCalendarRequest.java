package com.backend.pbl6schoolsystem.request.calendar;

import com.backend.pbl6schoolsystem.request.PageRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListCalendarRequest extends PageRequest {
    private Long userId;
    private Long classId;
    private String calendarEvent;
    private String calendarType;
    private Long semesterId;
    private Long schoolYearId;
}
