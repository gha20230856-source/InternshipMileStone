package com.example.InternshipMileStone.controller;


import com.example.InternshipMileStone.model.Employee;
import com.example.InternshipMileStone.model.dto.response.AttendanceResponseDTO;
import com.example.InternshipMileStone.repo.EmployeeRepo;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.InternshipMileStone.service.AttendanceService;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
public class AttendanceController {
    private AttendanceService attendanceService;
    private EmployeeRepo employeeRepo;


    @PostMapping("/check-in")
    public ResponseEntity<AttendanceResponseDTO> checkIn(Authentication authentication) {
        Long employeeId = extractEmployeeId(authentication);
        return ResponseEntity.ok(attendanceService.checkIn(employeeId));
    }

    @PostMapping("/check-out")
    public ResponseEntity<AttendanceResponseDTO> checkOut(Authentication authentication) {
        Long employeeId = extractEmployeeId(authentication);
        return ResponseEntity.ok(attendanceService.checkOut(employeeId));
    }


    @GetMapping("Admin/history")
    public ResponseEntity<List<AttendanceResponseDTO>> getAttendanceHistory(
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<AttendanceResponseDTO> history = attendanceService.getAttendanceHistory(employeeId, departmentId, startDate, endDate);
        return ResponseEntity.ok(history);
    }




    private Long extractEmployeeId(Authentication authentication) {
        // Parse user ID from JWT principal
        Long userId = Long.parseLong(authentication.getName());

        // Fetch Employee mapped to this userId
        return employeeRepo.findByUserId(userId)
                .map(Employee::getId)
                .orElseThrow(() -> new RuntimeException("No Employee profile associated with User ID: " + userId));
    }
}
