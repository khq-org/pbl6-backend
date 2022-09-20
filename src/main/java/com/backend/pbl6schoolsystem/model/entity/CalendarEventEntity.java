package com.backend.pbl6schoolsystem.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "calendarevent")
public class CalendarEventEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "calendareventid")
    private Long calendarEventId;
    @Column(name = "calendarevent")
    private String calendarEvent;
    @Column(name = "calendareventtype")
    private String calendarEventType;
    @Column(name = "starttime")
    private Timestamp startTime;
    @Column(name = "endtime")
    private Timestamp endTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subjectid", referencedColumnName = "subjectid")
    private SubjectEntity subject;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roomid", referencedColumnName = "roomid")
    private RoomEntity room;
    @Column(name = "createddate")
    private Timestamp createdDate;
    @Column(name = "modifieddate")
    private Timestamp modifiedDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdby", referencedColumnName = "userid")
    private UserEntity createdBy;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modifiedby", referencedColumnName = "userid")
    private UserEntity modifiedBy;
}
