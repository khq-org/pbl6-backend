package com.backend.pbl6schoolsystem.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "profile")
public class ProfileStudentEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profilestudentid")
    private Long profileStudentId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentid", referencedColumnName = "userid")
    private UserEntity student;
    @OneToMany(mappedBy = "profileStudent", cascade = CascadeType.ALL)
    private List<LearningResultEntity> learningResults;
    @Column(name = "createddate")
    private Timestamp createdDate;
    @Column(name = "updateddate")
    private Timestamp updateddate;
}
