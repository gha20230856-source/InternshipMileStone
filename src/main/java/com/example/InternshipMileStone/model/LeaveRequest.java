package com.example.InternshipMileStone.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "leave_request")
public class LeaveRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "employee_id")
    private Employee applicant;

    @Column( name = "leave_type",length = 30 , nullable = false,check = @CheckConstraint(name ="InLeaveList" , constraint = "leave_type IN ('SICK','CASUAL','EARNED','UNPAID')"))
    private String leaveType;

    @Column(name="start_date",nullable = false)
    private LocalDate startDate;

    @Column(name="end_date",nullable = false)
    private LocalDate endDate;

    @Column
    private String reason;

    @Column(length = 20,check = @CheckConstraint(name = "InStatusAttendenceList",constraint = "status in ('PENDING','APPROVED','REJECTED')"))
    @ColumnDefault("'PENDING'")
    private  String status;

    @CreationTimestamp
    @Column(name="applied_on",nullable = false)
    private LocalDateTime appliedOn;

    @ManyToOne(optional = true)
    @JoinColumn(name = "reviewed_by")
    private Employee reviewer;










}
