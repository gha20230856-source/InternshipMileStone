package com.example.InternshipMileStone.model.dto.common;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;


public record EmployeeCreatedDTO (

        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        @Email(message = "Email must be valid")
        String email,

        String designation,

        @Positive
        BigDecimal salary,

        String departmentName
)
{}