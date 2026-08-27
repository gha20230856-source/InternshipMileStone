package com.example.InternshipMileStone.model.dto.request;

import com.example.InternshipMileStone.model.Attendance;
import com.example.InternshipMileStone.model.Department;
import com.example.InternshipMileStone.model.LeaveRequest;
import com.example.InternshipMileStone.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

public record EmployeeUpdateDTO(

         String firstName,
         String lastName,
         @Email
         String email,
         String phone,
         String address,
         String designation,
         BigDecimal salary,
         String status,
         String departmentName

) {
}
