package com.backend.pbl6schoolsystem.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid")
    private Long userId;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "firstname")
    private String firstName;
    @Column(name = "lastname")
    private String lastName;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "phone")
    private String phone;
    @Column(name = "gender")
    private Boolean gender;
    @Column(name = "avatar")
    private String avatar;
    @Column(name = "dateofbirth")
    private LocalDate dateOfBirth;
    @Column(name = "placeofbirth")
    private String placeOfBirth;
    @Column(name = "nationality")
    private String nationality;
    @Column(name = "street")
    private String street;
    @Column(name = "district")
    private String district;
    @Column(name = "city")
    private String city;
    @Column(name = "job")
    private String job;
    // ==================== for student ============================
    @Column(name = "studentid")
    private String studentId;
    // ==================== for teacher ============================
    @Column(name = "teacherId")
    private String teacherId;
    @Column(name = "recruitmentDay")
    private LocalDate recruitmentDay;
    @Column(name = "numOfPeriodsInWeek")
    private Integer numOfPeriodsInWeek;
    @Column(name = "workingposition")
    private String workingPosition;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subjectid", referencedColumnName = "subjectid")
    private SubjectEntity subject;
    // =============================================================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roleid", referencedColumnName = "roleid")
    private RoleEntity role;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schoolid", referencedColumnName = "schoolid")
    private SchoolEntity school;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "parentstudent"
            , joinColumns = @JoinColumn(name = "parentid", referencedColumnName = "userid")
            , inverseJoinColumns = @JoinColumn(name = "studentid", referencedColumnName = "userid"))
    private List<UserEntity> childrens;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "profilestudentid", referencedColumnName = "profilestudentid")
    private ProfileStudentEntity profileStudent;
    @Column(name = "createddate")
    private Timestamp createdDate;
    @Column(name = "modifieddate")
    private Timestamp modifiedDate;
    @Column(name = "createdby")
    private Long createdBy;
    @Column(name = "modifiedby")
    private Long modifiedBy;
}
