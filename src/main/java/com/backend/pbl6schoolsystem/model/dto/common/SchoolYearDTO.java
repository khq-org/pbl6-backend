package com.backend.pbl6schoolsystem.model.dto.common;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchoolYearDTO implements Serializable {
    private Long schoolYearId;
    private LocalDate startDate;
    private LocalDate endDate;
}
