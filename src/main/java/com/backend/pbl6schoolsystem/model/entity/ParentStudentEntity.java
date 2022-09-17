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
@Table(name = "parentstudent")
public class ParentStudentEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parentstudentid")
    private Long parentStudentId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentid", referencedColumnName = "userid", nullable = false)
    private UserEntity parent;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentid", referencedColumnName = "userid", nullable = false)
    private UserEntity student;
}
