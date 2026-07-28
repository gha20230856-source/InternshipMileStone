package com.example.InternshipMileStone.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DialectOverride;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@Scope("prototype")
@Entity
@Table(name = "pay_roll")
public class Payroll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;


    @Column(nullable = false,check = @CheckConstraint(name = "MonthRangeCheck" , constraint = "month >=1 AND month <=12 "))
    private int month ;

    @Column(nullable = false)
    private int year;

    @Column(name="basic_salary" , precision = 12 , scale = 2)
    private BigDecimal basicSalary;

    @Column( precision = 12 , scale = 2)
    @ColumnDefault("0")
    private BigDecimal deductions;

    @Column(name="net_salary" , precision = 12 , scale = 2)
    private BigDecimal netSalary;

    @CreationTimestamp
    @Column(name = "generated_on")
    private LocalDateTime generatedOn;


    @ManyToOne(optional = false)
    private Employee employee;






}
