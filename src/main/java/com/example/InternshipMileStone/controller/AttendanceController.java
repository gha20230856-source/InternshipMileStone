package com.example.InternshipMileStone.controller;


import com.example.InternshipMileStone.model.Employee;
import com.example.InternshipMileStone.model.User;
import com.example.InternshipMileStone.model.dto.common.LeaveBalanceDTO;
import com.example.InternshipMileStone.model.dto.request.LeaveApplyRequestDTO;
import com.example.InternshipMileStone.model.dto.response.AttendanceResponseDTO;
import com.example.InternshipMileStone.model.dto.response.LeaveResponseDTO;
import com.example.InternshipMileStone.repo.EmployeeRepo;
import com.example.InternshipMileStone.repo.UserRepo;
import com.example.InternshipMileStone.service.LeaveService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.example.InternshipMileStone.service.AttendanceService;

import java.time.LocalDate;
import java.util.List;

//TODO implement a proper mapper
@RestController
@AllArgsConstructor
public class AttendanceController {
    private AttendanceService attendanceService;
    private EmployeeRepo employeeRepo;
    private LeaveService leaveService;
    private UserRepo userRepo;



    // works fine but the user needs to manually checkin
    @PostMapping("/check-in")
    public ResponseEntity<AttendanceResponseDTO> checkIn(Authentication authentication) {
        Long employeeId = extractEmployeeId(authentication);
        return ResponseEntity.ok(attendanceService.checkIn(employeeId));
    }

    @  PostMapping("/check-out")
    public ResponseEntity<AttendanceResponseDTO> checkOut(Authentication authentication) {
        Long employeeId = extractEmployeeId(authentication);
        return ResponseEntity.ok(attendanceService.checkOut(employeeId));
    }


    @GetMapping("admin/history")
    public ResponseEntity<List<AttendanceResponseDTO>> getAttendanceHistory(
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) String departmentName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<AttendanceResponseDTO> history = attendanceService.getAttendanceHistory(employeeId, departmentName, startDate, endDate);
        return ResponseEntity.ok(history);
    }




    private Long extractEmployeeId(Authentication authentication) {
        // get user name
        String  userName = (authentication.getName());

        // Fetch Employee mapped to this userId
        return employeeRepo.findEmployeeByUser_Username(userName)
                .map(Employee::getId)
                .orElseThrow(() -> new RuntimeException("No Employee profile associated with Employee name: " + userName));
    }

    @PostMapping("/apply")
    public ResponseEntity<LeaveResponseDTO> applyForLeave(
            Authentication authentication,
            @RequestBody LeaveApplyRequestDTO request) {
        Long userId = extractUserId(authentication);
        return new ResponseEntity<>(leaveService.applyForLeave(userId, request), HttpStatus.CREATED);
    }

    @GetMapping("/my-leaves")
    public ResponseEntity<List<LeaveResponseDTO>> getMyLeaves(Authentication authentication) {
        Long userId = extractUserId(authentication);
        return ResponseEntity.ok(leaveService.getLeavesByEmployee(userId));
    }

    @GetMapping("/balance")
    public ResponseEntity<LeaveBalanceDTO> getLeaveBalance(
            Authentication authentication,
            @RequestParam(required = false) Integer year) {
        Long userId = extractUserId(authentication);
        return ResponseEntity.ok(leaveService.getLeaveBalance(userId, year));
    }



    @PutMapping("admin/{id}/status/{status}")
    public ResponseEntity<LeaveResponseDTO> reviewLeaveRequest(
            Authentication authentication,
            @PathVariable("id") Long leaveId,
            @PathVariable  String status) {
        Long reviewerUserId = extractUserId(authentication);
        return ResponseEntity.ok(leaveService.reviewLeaveRequest(reviewerUserId, leaveId, status));
    }

    @GetMapping("admin/AllLeaves")
    public ResponseEntity<List<LeaveResponseDTO>> getAllLeaves(
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(leaveService.getAllLeaves(status));
    }

    private Long extractUserId(Authentication authentication) {

        String  userName = (authentication.getName());
        User user = userRepo.findByUsername(userName).orElseThrow(() -> new RuntimeException("No User associated with Username " + userName));
        return user.getId();

    }

}
