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
@Table(name = "devicesubject")
public class DeviceSubjectEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "devicesubjectid")
    private Long deviceSubjectId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deviceid", referencedColumnName = "deviceid")
    private DeviceEntity device;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subjectid", referencedColumnName = "subjectid")
    private SubjectEntity subject;
}
