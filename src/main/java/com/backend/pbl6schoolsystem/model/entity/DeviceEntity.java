package com.backend.pbl6schoolsystem.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "device")
public class DeviceEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deviceid")
    private Long deviceId;
    @Column(name = "device")
    private String device;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schoolid", referencedColumnName = "schoolid")
    private SchoolEntity school;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "status")
    private String status;
}
