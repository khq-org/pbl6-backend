package com.backend.pbl6schoolsystem.service;

import com.backend.pbl6schoolsystem.request.calendar.ListCalendarRequest;
import com.backend.pbl6schoolsystem.response.calendar.ListCalendarResponse;

public interface CalendarService {
    ListCalendarResponse getListCalendar(ListCalendarRequest request);
}
