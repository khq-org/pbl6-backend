package com.backend.pbl6schoolsystem.response.school;

import com.backend.pbl6schoolsystem.model.dto.common.SchoolDTO;
import com.backend.pbl6schoolsystem.response.PageResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "set")
public class ListSchoolResponse {
    private PageResponse pageResponse;
    private List<SchoolDTO> items;
}
