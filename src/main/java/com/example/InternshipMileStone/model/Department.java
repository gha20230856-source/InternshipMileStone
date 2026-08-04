package com.example.InternshipMileStone.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name= "department")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
        private Long id;

    @Column(length = 100,nullable = false,unique = true)
    private String name;

    @Column
    private String description;

    @OneToOne(optional = true)
    @JoinColumn(name = "department_head_id")
    @JsonIgnoreProperties("department")
    private Employee departmentHead;


    @OneToMany(mappedBy = "department",fetch = FetchType.LAZY)
    @JsonIgnoreProperties("department")
    private List<Employee> employeeList;






}
