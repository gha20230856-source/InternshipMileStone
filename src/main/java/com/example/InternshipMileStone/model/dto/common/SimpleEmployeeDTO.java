package com.example.InternshipMileStone.model.dto.common;

import com.example.InternshipMileStone.model.Attendance;
import com.example.InternshipMileStone.model.Department;
import com.example.InternshipMileStone.model.LeaveRequest;
import com.example.InternshipMileStone.model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record SimpleEmployeeDTO(
        @Email
        String email,

        String phone,


        String address,


        LocalDate dateOfJoining,


        String designation,


        BigDecimal salary,


        String status,
        User user
) {
}
