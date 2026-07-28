package com.example.InternshipMileStone.model;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DialectOverride;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@Scope("prototype")
@Entity
@Table(name= "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name="name",unique = true,nullable = false,length = 50)
    private String username;

    @Column(name="email",unique = true,nullable = false,length = 100)
    private String email;

    @Column(name="password",nullable = false)
    private String password;


    //TODO change fetching type of @ManyToOne to Lazy ( default eager)
    @ManyToOne
    private Role role;

    @Column(name="enabled")
    @ColumnDefault("True")
    private Boolean enabled;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "user")
    private Employee employee;



}
