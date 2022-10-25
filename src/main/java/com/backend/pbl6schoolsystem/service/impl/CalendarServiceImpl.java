package com.backend.pbl6schoolsystem.service.impl;

import com.backend.pbl6schoolsystem.repository.dsl.CalendarDslRepository;
import com.backend.pbl6schoolsystem.repository.jpa.CalendarRepository;
import com.backend.pbl6schoolsystem.repository.jpa.UserCalendarRepository;
import com.backend.pbl6schoolsystem.request.calendar.ListCalendarRequest;
import com.backend.pbl6schoolsystem.response.calendar.ListCalendarResponse;
import com.backend.pbl6schoolsystem.service.CalendarService;
import com.backend.pbl6schoolsystem.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {
    private static CalendarRepository calendarRepository;
    private static UserCalendarRepository userCalendarRepository;
    private static CalendarDslRepository calendarDslRepository;

    @Override
    public ListCalendarResponse getListCalendar(ListCalendarRequest request) {
        request.setCalendarEvent(RequestUtil.blankIfNull(request.getCalendarEvent()));
        request.setUserId(RequestUtil.defaultIfNull(request.getUserId(), -1L));
        request.setClassId(RequestUtil.defaultIfNull(request.getClassId(), -1L));
        return null;
    }
}
