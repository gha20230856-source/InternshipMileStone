package com.example.InternshipMileStone.model.dto.response;


import java.time.LocalDate;
import java.time.LocalDateTime;

public record AttendanceResponseDTO(
        Long id,
        Long employeeId,
        String employeeName,
        String departmentName,
        LocalDate attendanceDate,
        String status,
        LocalDateTime checkInTime,
        LocalDateTime checkOutTime,
        Double hoursWorked
){}