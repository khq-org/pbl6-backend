package com.backend.pbl6schoolsystem.controller.calendar;

import com.backend.pbl6schoolsystem.converter.CalendarConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/calendars")
@RequiredArgsConstructor
public class CalendarController {
    private final CalendarConverter calendarConverter;
}
