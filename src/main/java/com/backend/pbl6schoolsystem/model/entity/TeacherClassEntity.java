package com.backend.pbl6schoolsystem.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "teacherclass")
public class TeacherClassEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacherclassid")
    private Long teacherClassId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacherid", referencedColumnName = "userid")
    private UserEntity teacher;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classid", referencedColumnName = "classid")
    private ClassEntity clazz;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schoolyearid", referencedColumnName = "schoolyearid")
    private SchoolYearEntity schoolYear;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semesterid", referencedColumnName = "semesterid")
    private SemesterEntity semester;
    @Column(name = "isclassleader")
    private Boolean isClassLeader;
}
