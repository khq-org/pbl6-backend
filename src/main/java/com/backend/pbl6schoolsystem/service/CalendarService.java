package com.backend.pbl6schoolsystem.service;

import com.backend.pbl6schoolsystem.request.calendar.CreateUpdateCalendarRequest;
import com.backend.pbl6schoolsystem.request.calendar.ListCalendarRequest;
import com.backend.pbl6schoolsystem.response.NoContentResponse;
import com.backend.pbl6schoolsystem.response.OnlyIdResponse;
import com.backend.pbl6schoolsystem.response.calendar.ListCalendarResponse;

public interface CalendarService {
    ListCalendarResponse getListCalendar(ListCalendarRequest request);

    OnlyIdResponse createCalendar(CreateUpdateCalendarRequest request);

    OnlyIdResponse updateCalendar(Long calendarId, CreateUpdateCalendarRequest request);

    NoContentResponse deleteCalendar(Long calendarId);
}
