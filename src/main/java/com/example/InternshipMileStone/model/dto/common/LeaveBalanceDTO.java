package com.example.InternshipMileStone.model.dto.common;

import jakarta.validation.constraints.Positive;

public record LeaveBalanceDTO(
        Long employeeId,
        String employeeName,
        int year,
        int totalAllowance,
        long usedDays,
        long remainingDays
) {}