package com.backend.pbl6schoolsystem.controller.user;

import com.backend.pbl6schoolsystem.converter.UserConverter;
import com.backend.pbl6schoolsystem.model.dto.common.MessageDTO;
import com.backend.pbl6schoolsystem.model.dto.user.UserInfoDTO;
import com.backend.pbl6schoolsystem.request.user.ChangePasswordRequest;
import com.backend.pbl6schoolsystem.response.NoContentResponse;
import com.backend.pbl6schoolsystem.response.Response;
import com.backend.pbl6schoolsystem.response.UserInfoResponse;
import com.backend.pbl6schoolsystem.security.UserPrincipal;
import com.backend.pbl6schoolsystem.service.UserService;
import com.backend.pbl6schoolsystem.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final UserConverter userConverter;

    @GetMapping
    public Response<UserInfoDTO> getMyInfo() {
        UserPrincipal principal = SecurityUtils.getPrincipal();
        UserInfoResponse response = userService.getInfoAccount(principal.getUserId());
        return userConverter.getResponse(response);
    }

    @PutMapping
    public Response<UserInfoDTO> updateMyInfo() {
        return null;
    }

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
