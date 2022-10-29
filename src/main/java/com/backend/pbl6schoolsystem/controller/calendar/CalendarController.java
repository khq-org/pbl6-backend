package com.backend.pbl6schoolsystem.controller.calendar;

import com.backend.pbl6schoolsystem.converter.CalendarConverter;
import com.backend.pbl6schoolsystem.model.dto.calendar.CalendarEventDTO;
import com.backend.pbl6schoolsystem.model.dto.common.ListDTO;
import com.backend.pbl6schoolsystem.model.dto.common.OnlyIdDTO;
import com.backend.pbl6schoolsystem.request.calendar.CreateUpdateCalendarRequest;
import com.backend.pbl6schoolsystem.request.calendar.ListCalendarRequest;
import com.backend.pbl6schoolsystem.response.Response;
import com.backend.pbl6schoolsystem.service.CalendarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Calendar", description = "Calendar APIs")
@RestController
@RequestMapping("/api/calendars")
@RequiredArgsConstructor
public class CalendarController {
    private final CalendarConverter calendarConverter;
    private final CalendarService calendarService;

    @Operation(summary = "List calendar event")
    @GetMapping
    public Response<ListDTO<CalendarEventDTO>> getListCalendarEvent(@Valid @ModelAttribute ListCalendarRequest request){
        return null;
    }

    @Operation(summary = "Create calendar event")
    @PostMapping
    public Response<OnlyIdDTO> createCalendarEvent(@Valid @RequestBody CreateUpdateCalendarRequest request) {
        return null;
    }
}
