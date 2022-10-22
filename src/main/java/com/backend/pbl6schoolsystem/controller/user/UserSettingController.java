package com.backend.pbl6schoolsystem.controller.user;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User Setting", description = "User Setting APIs")
@RestController
@RequestMapping("/api/users/setting")
@RequiredArgsConstructor
public class UserSettingController {
}
