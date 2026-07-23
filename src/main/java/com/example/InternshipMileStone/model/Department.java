package com.example.InternshipMileStone.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@Scope("prototype")
@Entity
@Table(name= "department")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
        private long id;

    @Column(length = 100,nullable = false)
    private String name;

    @Column
    private String description;

    @OneToOne(optional = true)
    @JoinColumn(name = "department_head_id")
    private Employee departmentHead;




}
