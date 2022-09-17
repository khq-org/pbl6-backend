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
@Table(name = "class")
public class ClassEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "classid")
    private Long classId;
    @Column(name = "class")
    private String clazz;
    @Column(name = "specializedclass")
    private Boolean isSpecializedClass;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gradeid", referencedColumnName = "gradeid", nullable = false)
    private GradeEntity grade;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schoolid", referencedColumnName = "schoolid", nullable = false)
    private SchoolEntity school;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacherid", referencedColumnName = "userid")
    private UserEntity teacher;
    @Column(name = "createddate")
    private Timestamp createdDate;
    @Column(name = "modifieddate")
    private Timestamp modifiedDate;
    @Column(name = "createdby")
    private Long createdBy;
    @Column(name = "modifiedby")
    private Long modifiedBy;
}
