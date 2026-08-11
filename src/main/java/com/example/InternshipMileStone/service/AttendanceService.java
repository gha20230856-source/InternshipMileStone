package com.example.InternshipMileStone.service;

import com.example.InternshipMileStone.model.Attendance;
import com.example.InternshipMileStone.model.Employee;
import com.example.InternshipMileStone.repo.EmployeeRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;

@Service
@Data
@AllArgsConstructor
public class AttendanceService {

    private EmployeeRepo employeeRepo;

    // TODO fix when you make jwt
    public void  checkIn(Principal principal) {
        Employee employee = employeeRepo.findEmployeeByUser_Username(principal.getName()).
                orElseThrow(()-> new EntityNotFoundException("how are you here with no user??"));
        Attendance attendance = new Attendance();

        attendance.setEmployee(employee);
        attendance.setStatus("");





    }
}
