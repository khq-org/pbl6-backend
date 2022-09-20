package com.backend.pbl6schoolsystem.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "teachercalendarevent")
public class TeacherCalendarEventEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teachercalendareventid")
    private Long teacherCalendarEventId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacherid", referencedColumnName = "userid")
    private UserEntity teacher;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendareventid", referencedColumnName = "calendareventid")
    private CalendarEventEntity calendarEvent;
}