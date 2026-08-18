package com.example.InternshipMileStone.model.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record LeaveResponseDTO(
        Long id,
        Long applicantId,
        String applicantName,
        String leaveType,
        LocalDate startDate,
        LocalDate endDate,
        long totalDays,
        String reason,
        String status,
        LocalDateTime appliedOn,
        Long reviewerId,
        String reviewerName
) {}