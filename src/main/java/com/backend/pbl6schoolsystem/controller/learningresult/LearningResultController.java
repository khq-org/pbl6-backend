package com.backend.pbl6schoolsystem.controller.learningresult;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Learning Result", description = "LearningResult APIs")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/learningresults")
public class LearningResultController {
}
