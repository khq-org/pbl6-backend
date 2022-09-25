package com.backend.pbl6schoolsystem.model.dto.calendar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties()
public class CalendarEventDTO implements Serializable {
}
