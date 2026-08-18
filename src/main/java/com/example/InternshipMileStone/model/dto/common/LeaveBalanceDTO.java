package com.example.InternshipMileStone.model.dto.common;

public record LeaveBalanceDTO(
        Long employeeId,
        String employeeName,
        int year,
        int totalAllowance,
        long usedDays,
        long remainingDays
) {}