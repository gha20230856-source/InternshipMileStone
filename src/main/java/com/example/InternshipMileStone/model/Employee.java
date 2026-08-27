package com.example.InternshipMileStone.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="first_name",length = 50,nullable = false)
    private String firstName;

    @Column(name="last_name", length = 50,nullable = false)
    private String lastName;


    //TODO Add email Validation as  a constraint
    @Column(name="email",length = 100,unique = true,nullable = false)
    private String email;

    @Column(name = "phone",length = 20)
    private String phone;

    @Column(name="address")
    private String address;

    @Column(name="date_of_joining",nullable = false)
    private LocalDate dateOfJoining;

    @Column(length = 50)
    private String designation;

    @Column(precision = 12,scale = 2)
    private BigDecimal salary;

    @Column( length = 20,check = @CheckConstraint(name="InListEmployeeStatus",constraint = "status IN ('Active','InActive')"))
    @ColumnDefault("'Active'")
    private String status;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;



    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(mappedBy = "employee")
    private List<Attendance> attendanceList;

    @OneToMany(mappedBy = "applicant")
    private List<LeaveRequest>leaveRequestList;






}
