package com.backend.pbl6schoolsystem.response.calendar;

import com.backend.pbl6schoolsystem.model.dto.calendar.CalendarEventDetailDTO;
import com.backend.pbl6schoolsystem.response.ErrorResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "set")
public class GetCalendarEventResponse {
    private Boolean success;
    private ErrorResponse errorResponse;
    private CalendarEventDetailDTO calendarEventDetail;
}
