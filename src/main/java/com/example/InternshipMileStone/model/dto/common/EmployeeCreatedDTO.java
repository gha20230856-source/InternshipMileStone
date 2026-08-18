package com.example.InternshipMileStone.model.dto.common;


import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;


public record EmployeeCreatedDTO (String firstName,
                                   String lastName,
                                   String email, // Shared for both User and Employee
                                   String designation,
                                    BigDecimal salary,
                                     String departmentName)
                                                            {}