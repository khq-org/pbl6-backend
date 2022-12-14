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
@Table(name = "learningresult")
public class LearningResultEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "learningresultid")
    private Long learningResultId;
    @ManyToOne
    @JoinColumn(name = "schoolyearid", referencedColumnName = "schoolyearid")
    private SchoolYearEntity schoolYear;
    @ManyToOne
    @JoinColumn(name = "classid", referencedColumnName = "classid")
    private ClassEntity clazz;
    @Column(name = "averagescore")
    private Double averageScore;
    @Column(name = "conduct")
    private String conduct;
    @Column(name = "learninggrading")
    private String learningGrading;
    @Column(name = "isPassed")
    private Boolean isPassed;
    @ManyToOne
    @JoinColumn(name = "profilestudentid", referencedColumnName = "profilestudentid")
    private ProfileStudentEntity profileStudent;
}
