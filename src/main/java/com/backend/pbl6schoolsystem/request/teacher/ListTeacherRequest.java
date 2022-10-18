package com.backend.pbl6schoolsystem.request.teacher;

import com.backend.pbl6schoolsystem.request.PageRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListTeacherRequest extends PageRequest {
    private String firstName;
    private String lastName;
    private String search;
    private String district;
    private Long classId;
    private String workingPosition;
}
