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
@Table(name = "usercalendarevent")
public class UserCalendarEventEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usercalendareventid")
    private Long userCalendarEventId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", referencedColumnName = "userid")
    private UserEntity user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendareventid", referencedColumnName = "calendareventid")
    private CalendarEventEntity calendarEvent;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schoolyearid", referencedColumnName = "schoolyearid")
    private SchoolYearEntity schoolYear;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semesterid", referencedColumnName = "semesterid")
    private SemesterEntity semester;
}