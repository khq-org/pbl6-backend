package com.backend.pbl6schoolsystem.controller.user;

import com.backend.pbl6schoolsystem.converter.UserConverter;
import com.backend.pbl6schoolsystem.model.dto.calendar.CalendarEventDTO;
import com.backend.pbl6schoolsystem.model.dto.common.ClazzDTO;
import com.backend.pbl6schoolsystem.model.dto.common.ListDTO;
import com.backend.pbl6schoolsystem.model.dto.common.MessageDTO;
import com.backend.pbl6schoolsystem.model.dto.common.OnlyIdDTO;
import com.backend.pbl6schoolsystem.model.dto.user.UserInfoDTO;
import com.backend.pbl6schoolsystem.request.calendar.ListCalendarRequest;
import com.backend.pbl6schoolsystem.request.user.ChangePasswordRequest;
import com.backend.pbl6schoolsystem.request.user.UpdateUserRequest;
import com.backend.pbl6schoolsystem.response.NoContentResponse;
import com.backend.pbl6schoolsystem.response.OnlyIdResponse;
import com.backend.pbl6schoolsystem.response.Response;
import com.backend.pbl6schoolsystem.response.UserInfoResponse;
import com.backend.pbl6schoolsystem.response.calendar.ListCalendarResponse;
import com.backend.pbl6schoolsystem.response.clazz.ListClassResponse;
import com.backend.pbl6schoolsystem.security.UserPrincipal;
import com.backend.pbl6schoolsystem.service.UserService;
import com.backend.pbl6schoolsystem.util.RequestUtil;
import com.backend.pbl6schoolsystem.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "My account", description = "My Account APIs")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final UserConverter userConverter;

    @Operation(summary = "Test Github Actions")
    @GetMapping("/github-actions")
    public String testGithubActions() {
        return new String("Test!");
    }

    @Operation(summary = "Get my information")
    @GetMapping
    public Response<UserInfoDTO> getMyInfo() {
        UserPrincipal principal = SecurityUtils.getPrincipal();
        UserInfoResponse response = userService.getInfoAccount(principal.getUserId());
        return userConverter.getResponse(response);
    }

    @Operation(summary = "Get my classes")
    @GetMapping("/classes")
    public Response<List<ClazzDTO>> getMyClasses() {
        ListClassResponse response = userService.getListMyClass();
        return userConverter.getResponse(response);
    }

    @Operation(summary = "List calendar event")
    @GetMapping("/calendar")
    public Response<ListDTO<CalendarEventDTO>> listCalendarEvent(@ModelAttribute @Valid ListCalendarRequest request) {
        request.setClassId(RequestUtil.defaultIfNull(request.getClassId(), -1L));
        request.setSemesterId(RequestUtil.defaultIfNull(request.getSemesterId(), -1L));
        ListCalendarResponse response = userService.getListCalendar(request);
        if (response.getSuccess()) {
            return userConverter.getResponse(response);
        }
        return userConverter.getError(response.getErrorResponse());
    }

    @Operation(summary = "Update my information")
    @PutMapping
    public Response<OnlyIdDTO> updateMyInfo(@RequestBody UpdateUserRequest request) {
        UserPrincipal principal = SecurityUtils.getPrincipal();
        OnlyIdResponse response = userService.updateInfoAccount(principal.getUserId(), request);
        if (response.getSuccess()) {
            return userConverter.getResponse(response);
        }
        return userConverter.getError(response.getErrorResponse());
    }

    @Operation(summary = "Change password")
    @PutMapping("/password")
    public Response<MessageDTO> changeMyPassword(@RequestBody ChangePasswordRequest request) {
        UserPrincipal principal = SecurityUtils.getPrincipal();
        NoContentResponse response = userService.changePassword(principal.getUserId(), request);
        if (response.getSuccess()) {
            return userConverter.getResponse(response);
        }
        return userConverter.getError(response.getErrorResponse());
    }
}
