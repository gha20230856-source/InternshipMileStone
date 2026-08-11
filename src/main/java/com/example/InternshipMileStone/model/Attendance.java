package com.example.InternshipMileStone.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="attendance")
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name="employee_id")
    private Employee employee;


    @Column(name="attendance_date",nullable = false)
    private LocalDate attendanceDate;

    @Column(nullable = false,check = @CheckConstraint(name = "InStatusList" , constraint = "status in ('PRESENT', 'ABSENT','LEAVE','HALF_DAY')"))
    private String status;

    @CreationTimestamp
    @Column(name = "check_in_time")
    private LocalDateTime checkInTime;


    @Column(name = "check_out_time")
    private LocalDateTime checkOutTime;


}
