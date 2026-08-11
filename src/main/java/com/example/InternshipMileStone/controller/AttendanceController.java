package com.example.InternshipMileStone.controller;

import com.example.InternshipMileStone.service.AttendanceService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@AllArgsConstructor
public class AttendanceController {
    private AttendanceService attendanceService;

    //TODO fix when you implement jwt
    @PostMapping("/CheckIn")
    public ResponseEntity<String> EmployeeCheckIn(Principal principal)
    {
        attendanceService.checkIn(principal);
        return ResponseEntity.ok("hi mister ");
    }
}
