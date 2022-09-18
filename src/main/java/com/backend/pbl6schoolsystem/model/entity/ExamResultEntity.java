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
@Table(name = "examresult")
public class ExamResultEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "examresultid")
    private Long examResultId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subjectid", referencedColumnName = "subjectid")
    private SubjectEntity subject;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentid", referencedColumnName = "userid")
    private UserEntity student;
    @Column(name = "examtype")
    private String examType;
    @Column(name = "score")
    private Double score;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semesterid", referencedColumnName = "semesterid")
    private SemesterEntity semester;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schoolyearid", referencedColumnName = "schoolyearid")
    private SchoolYearEntity schoolYear;
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
